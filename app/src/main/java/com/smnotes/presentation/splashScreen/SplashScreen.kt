package com.smnotes.presentation.splashScreen

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

@Composable
fun SplashScreen(
    isReady: Boolean,
    onFinished: () -> Unit
) {
    LaunchedEffect(isReady) {
        if (isReady) onFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple50),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val compositionResult: LottieCompositionResult =
            rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_logo))

        LottieAnimation(
            compositionResult.value, isPlaying = true,
            iterations = 3,
            speed = 1.5f
        )

        Image(painterResource(id = R.drawable.smnotes_purple), "")
    }
}




