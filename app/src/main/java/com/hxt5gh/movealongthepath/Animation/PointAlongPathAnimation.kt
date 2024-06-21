package com.hxt5gh.movealongthepath.Animation

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random


@Composable
fun PointAlongPathAnimation(modifier: Modifier = Modifier) {
    val totalCount = 8
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    var largePoint = remember {
        List(totalCount) {
            mutableStateOf(
                Offset(
                    x = (100f) * Random.nextFloat() * 8 + 200,
                    y = (100f) * Random.nextFloat() * 8 + 200
                )
            )
        }
    }

    val smallPointAnimX = List(totalCount) { rememberInfiniteTransition(label = "xPosition") }
    val smallPointAnimY = List(totalCount) { rememberInfiniteTransition(label = "yPosition") }

    val randomColor = remember {
        List(totalCount) {
            mutableStateOf(listOf(
                Color.Red,
                Color.Black,
                Color.Blue,
                Color.Cyan,
                Color.Green,
                Color.Yellow,
                Color.LightGray,
                Color.Magenta,
            ).random())
        }
    }
    var ondrag by remember {
        mutableStateOf(false)
    }

    var centerScreen = remember {
        Offset.Zero
    }
    Box(modifier = modifier.onSizeChanged {
        centerScreen = centerScreen.copy(
            x = it.width / 2f,
            y = it.height / 2f
        )

    }) {

        //large points
        largePoint.forEachIndexed { index, mutableState ->
            Canvas(modifier = Modifier
                .offset {
                    IntOffset(
                        mutableState.value.x.roundToInt(),
                        mutableState.value.y.roundToInt()
                    )
                }
                .size(40.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            ondrag = true
                        },
                        onDragEnd = {
                            ondrag = false

                        },
                        onDragCancel = {
                            ondrag = false
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        mutableState.value = mutableState.value.copy(
                            x = mutableState.value.x + dragAmount.x,
                            y = mutableState.value.y + dragAmount.y
                        )
                    }
                }
            ) {
                drawCircle(
                    color = randomColor[index].value,
                    radius = 15.dp.value * density.density,
                    center = center
                )
            }


        }


        if (!ondrag) {
            //small[points
            largePoint.forEachIndexed { index, mutableState ->
                val offsetProgressX = smallPointAnimX[index].animateFloat(
                    initialValue = largePoint[index].value.x,
                    targetValue = largePoint[(index + 1) % largePoint.size].value.x,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = EaseIn),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = ""
                )
                val offsetProgressY = smallPointAnimY[index].animateFloat(
                    initialValue = largePoint[index].value.y,
                    targetValue = largePoint[(index + 1) % largePoint.size].value.y,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = EaseIn),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = ""
                )

                Canvas(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                offsetProgressX.value.roundToInt(),
                                offsetProgressY.value.roundToInt()
                            )
                        }
                        .size(40.dp)
                ) {
                    drawCircle(
                        color = Color.Black,
                        radius = 5.dp.value * density.density
                    )
                }
            }
        }

        //setting up lines
        for (i in 0 until totalCount) {
            val startX = largePoint[i].value.x
            val startY = largePoint[i].value.y
            val endX = largePoint[(i + 1) % totalCount].value.x
            val endY = largePoint[(i + 1) % totalCount].value.y
            Canvas(modifier = Modifier) {
                drawLine(
                    color = Color.Red,
                    start = Offset(x = startX + 20.dp.toPx(), y = startY + 20.dp.toPx()),
                    end = Offset(x = endX + 20.dp.toPx(), y = endY + 20.dp.toPx()),
                    strokeWidth = 2.dp.toPx()
                )
            }
        }

    }
}

fun calculatePoint(center: Offset, angle: Float, radius: Float): Offset {
    val radians = Math.toRadians(angle.toDouble()).toFloat()
    val x = center.x + radius * cos(radians)
    val y = center.y + radius * sin(radians)
    return Offset(x, y)
}

@Preview(showBackground = true)
@Composable
private fun MovePointPrev() {
    PointAlongPathAnimation(modifier = Modifier.fillMaxSize())
}