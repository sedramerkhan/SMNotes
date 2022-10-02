package com.smnotes.presentation.notesScreen.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.smnotes.R

@Composable
fun MainDrawer(
    selected: DrawerItems,
    onItemSelected: (DrawerItems) -> Unit,
) {
    val round = 40.dp
    Surface(
        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(topEnd = round, bottomEnd = round)
    ) {
        Column(
            modifier = Modifier.padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopDrawerView()
            DrawerItemsView(selected = selected, onItemSelected = onItemSelected)
        }
    }
}


@Composable
fun TopDrawerView() {
    Card( modifier = Modifier.clip(CircleShape)){
        Image(
           modifier = Modifier.size(150.dp) ,
            painter = painterResource(id = R.drawable.img), contentDescription = null,

            )
    }

    Divider(Modifier.fillMaxWidth(.8f).padding(top=8.dp,bottom=16.dp))

}

@Composable
fun DrawerItemsView(
    selected: DrawerItems,
    onItemSelected: (DrawerItems) -> Unit,
) {
    getDrawerItems().forEach {
        val color = MaterialTheme.colors.primary.copy(alpha = 0.2f)
        val color2 = MaterialTheme.colors.primary
        val round = RoundedCornerShape(20.dp)
        val width =
            animateDpAsState(
                targetValue = if (selected == it) 28.dp else 2.dp, animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearEasing,
//                        delayMillis = 500
                )
            )
        Box(
            Modifier
                .fillMaxWidth(.9f)
                .clickable { onItemSelected(it) }
//                    .background(backgroundColor.value)
        ) {
            Box(
                Modifier
                    .matchParentSize()
                    .border(width.value, color, round)
            ) {
            }
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = it.icon, contentDescription = it.value, tint = color2)
                Text(
                    text = it.value,
                    style = MaterialTheme.typography.h3,
                    color = color2
                )
            }

        }
        Spacer(Modifier.height(16.dp))
    }
}