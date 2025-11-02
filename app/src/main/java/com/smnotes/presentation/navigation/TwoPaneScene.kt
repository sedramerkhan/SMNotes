package com.smnotes.presentation.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

class TwoPaneScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val firstEntry: NavEntry<T>,
    val secondEntry: NavEntry<T>
) : Scene<T> {

    override val entries: List<NavEntry<T>>
        get() = listOf(firstEntry, secondEntry)

    override val content: @Composable (() -> Unit)
        get() = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.4f)
                ) {
                    firstEntry.Content()
                }
                Box(
                    modifier = Modifier
                        .weight(0.6f)
                ) {
                    secondEntry.Content()
                }
            }
        }

    companion object {
        const val TWO_PANE_KEY = "TwoPaneKey"
        fun twoPane() = mapOf(TWO_PANE_KEY to true)
    }
}

@Composable
fun <T : Any> rememberTwoPaneSceneStrategy(): TwoPaneSceneStrategy<T> {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    return remember(windowSizeClass) {
        TwoPaneSceneStrategy(windowSizeClass)
    }
}


class TwoPaneSceneStrategy<T : Any>(val windowSizeClass: WindowSizeClass) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {

        if (!windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) {
            return null
        }

        val lastTwoEntries = entries.takeLast(2)
        val hasTwoPaneKey = lastTwoEntries.all {
            it.metadata.containsKey(TwoPaneScene.TWO_PANE_KEY) && it.metadata[TwoPaneScene.TWO_PANE_KEY] == true
        }

        return if (lastTwoEntries.size == 2 && hasTwoPaneKey) {
            val firstEntry = lastTwoEntries.first()
            val secondEntry = lastTwoEntries.last()

            TwoPaneScene(
                key = firstEntry.contentKey to secondEntry.contentKey,
                previousEntries = entries.dropLast(1),
                firstEntry = firstEntry,
                secondEntry = secondEntry
            )
        } else null
    }

}


