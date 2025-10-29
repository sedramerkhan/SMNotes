package com.smnotes.presentation.splashScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.airbnb.lottie.compose.*
import com.smnotes.R
import com.smnotes.presentation.theme.Purple50
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple50),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val compositionResult: LottieCompositionResult =
            rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_logo)) //it can be stored in asset too

        LaunchedEffect(key1 = true) {
            delay(3200)
            Log.i("lottie", "hello after 1500")
            onFinished()
        }

        LottieAnimation(
            compositionResult.value, isPlaying = true,
            iterations = 3,
            speed = 1.5f
        )

        Image(painterResource(id = R.drawable.smnotes_purple), "")

    }
}




