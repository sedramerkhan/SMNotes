package com.smnotes.presentation.splashScreen

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.airbnb.lottie.compose.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.smnotes.R
import com.smnotes.presentation.destinations.NotesScreenDestination
import com.smnotes.presentation.theme.Purple50
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator
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

        val progress by animateLottieCompositionAsState(
            compositionResult.value,
            isPlaying = true,
            iterations = 3,
            speed = 1.5f
        )
        LaunchedEffect(key1 =true ) {
            delay(3200)
            Log.i("lottie","hello after 1500")
            navigator.popBackStack()
            navigator.navigate(NotesScreenDestination)
        }

        LottieAnimation(compositionResult.value, progress)

        Image(painterResource(id = R.drawable.smnotes_purple), "")

    }
}




