package com.smnotes.data.remote

import com.smnotes.data.auth.SessionStore
import com.smnotes.data.remote.dto.auth.AuthResponseDto
import com.smnotes.data.remote.dto.auth.RefreshRequestDto
import com.smnotes.domain.event.AuthEvent
import com.smnotes.domain.event.AuthEventBus
import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

fun provideHttpClient(tokenStore: SessionStore, baseUrl: String): HttpClient =
    HttpClient(OkHttp) {
        install(ContentNegotiation) { json() } //ContentNegotiation is a Ktor plugin that handles automatic serialization/deserialization of request and response bodies.
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("HttpLog", message)
                }
            }
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val access = tokenStore.accessToken ?: return@loadTokens null
                    val refresh = tokenStore.getRefreshToken() ?: return@loadTokens null
                    BearerTokens(access, refresh)
                }
                refreshTokens {
                    val refreshToken = oldTokens?.refreshToken ?: return@refreshTokens null
                    val response = client.post("$baseUrl/auth/refresh") {
                        markAsRefreshTokenRequest() //skip the loadTokens / bearer header injection for this specific request
                        setBody(RefreshRequestDto(refreshToken))
                        contentType(ContentType.Application.Json)
                    }
                    if (response.status == HttpStatusCode.OK) {
                        val dto = response.body<AuthResponseDto>()
                        tokenStore.saveTokens(dto.accessToken, dto.refreshToken)
                        BearerTokens(dto.accessToken, dto.refreshToken)
                    } else {
                        tokenStore.clearAll()
                        AuthEventBus.events.tryEmit(AuthEvent.SessionExpired)
                        null
                    }
                }
                sendWithoutRequest { request ->
                    !request.url.encodedPath.startsWith("/auth")
                }
            }
        }
    }
