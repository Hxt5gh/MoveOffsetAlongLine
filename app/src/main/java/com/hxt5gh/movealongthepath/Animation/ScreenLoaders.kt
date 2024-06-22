package com.hxt5gh.movealongthepath.Animation

import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hxt5gh.movealongthepath.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLoaders(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Screen Loaders") })}
    ) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            LazyColumn(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    Divider()
                CircularLoader(modifier = Modifier.size(80.dp))
                    Divider()
                }
                item {
                    Divider()
                    InfiniteTransition(modifier = Modifier.size(150.dp))
                    Divider()
                }
                item {
                    Divider()
                    InfiniteBeatTransition(modifier = Modifier.size(150.dp))
                    Divider()
                }
                item {
                    Divider()
                    RollTransition(modifier = Modifier
                        .size(150.dp)
                        .fillMaxWidth()
                    )
                    Divider()
                }
                item {
                    Divider()
                    RotateTransition(modifier = Modifier
                        .size(150.dp)
                        .fillMaxWidth()
                    )
                    Divider()
                }
            }
        }
    }
}



@Composable
fun CircularLoader(modifier: Modifier = Modifier) {

    val updateTransition = rememberInfiniteTransition(label = "")
    val rotationValue = updateTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(800)
        ),
        label = ""
    )
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier.size(46.dp)) {


            val size = this.size
            val x = size.width / 2
            val y = size.height / 2

            rotate(
                degrees =  rotationValue.value ,
                pivot = Offset(x, y)
            ) {
                drawArc(
                    color = Color(219, 81, 243, 252),
                    startAngle = 90f, //clockwise
                    sweepAngle = 270f,
                    useCenter = false,
                    topLeft = Offset.Zero,
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Square)
                )
            }

        }
    }
}

@Composable
fun InfiniteTransition(modifier: Modifier = Modifier) {
    Box(modifier.padding(8.dp), contentAlignment = Alignment.Center){

        var bool by remember {
            mutableStateOf(true)
        }

        val transition = rememberInfiniteTransition(label = "")

        val value = transition.animateFloat(
            initialValue = 0f,
           // targetValue = if(bool) 360f else 0f ,
            targetValue =  360f  ,
            animationSpec = infiniteRepeatable(
                animation = tween(3000 , easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = ""
        )

        val color = transition.animateColor(
            initialValue =  Color.Blue,
            targetValue = Color(0xFFBB86FC),
            animationSpec = infiniteRepeatable(
                animation = tween(1500 , easing = EaseInQuart),
                repeatMode = RepeatMode.Reverse),
            label = ""
        )
            Canvas(modifier = Modifier.size(120.dp)
            ) {
                rotate(
                    degrees =3 * value.value
                ){
                    drawArc(
                        // color = color.value,
                        brush = Brush.linearGradient(
                            listOf(
                                Color.Blue,
                                Color(0xFFBB86FC)

                            )
                        ),
                        startAngle = 0f,
                        sweepAngle = value.value,
                        useCenter = false,
                        topLeft = Offset.Zero,
                        style = Stroke(10.dp.toPx() , cap = StrokeCap.Round)
                    )
                }

        }
    }
}

@Composable
fun InfiniteBeatTransition(modifier: Modifier) {

    val transition1 = remember {
        Animatable(initialValue = 0f)
    }
    val transition2 = remember {
        Animatable(initialValue = 0f)
    }
    val transition3 = remember {
        Animatable(initialValue = 0f)
    }

    LaunchedEffect(key1 = transition1) {

        launch {
            transition3.animateTo(
                targetValue =  360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1800, delayMillis = 0 , easing = EaseInBack),
                    repeatMode = RepeatMode.Restart,
                )
            )
        }
        launch {
            transition2.animateTo(
                targetValue =  360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200,  delayMillis = 600, easing = EaseInBack ),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
        launch {
            transition1.animateTo(
                targetValue =  360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, delayMillis = 1200 , easing = EaseInBack ),
                    repeatMode = RepeatMode.Restart
                )

            )
        }
    }




    Box(modifier = modifier , contentAlignment = Alignment.Center){
        Canvas(modifier = Modifier
            .size(150.dp)
            .padding(38.dp)) {
            val size = this.size
            val x = size.width / 2
            val y = size.height / 2
            rotate(
                degrees = transition1.value
            ){

            drawArc(
                color = Color.Black,
                startAngle = 210f,
                sweepAngle = 120f,
                useCenter = false,
                topLeft = Offset.Zero,
                style = Stroke(8.dp.toPx() , cap = StrokeCap.Round),
            )
            }
        }
        Canvas(modifier = Modifier
            .size(125.dp)
            .padding(38.dp)) {
            val size = this.size
            val x = size.width / 2
            val y = size.height / 2

            rotate(
                degrees = transition2.value
            ){

            drawArc(
                color = Color.Black,
                startAngle = 210f,
                sweepAngle = 120f,
                useCenter = false,
                topLeft = Offset.Zero,
                style = Stroke(8.dp.toPx() , cap = StrokeCap.Round),
            )
            }

        }
        Canvas(modifier = Modifier
            .size(100.dp)
            .padding(38.dp)) {
            val size = this.size
            val x = size.width / 2
            val y = size.height / 2
            rotate(
                degrees = transition3.value
            ){
                drawArc(
                    color = Color.Black,
                    startAngle = 210f,
                    sweepAngle = 120f,
                    useCenter = false,
                    topLeft = Offset.Zero,
                    style = Stroke(8.dp.toPx() , cap = StrokeCap.Round),
                )
            }

        }
    }

}

@Composable
fun RollTransition(modifier: Modifier) {

    val updates = rememberInfiniteTransition(label = "")
    val first = updates.animateFloat(
        initialValue = 1000f,//1000
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 4000,
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )
    Canvas(
        modifier = modifier
            .padding(24.dp)
    ) {
        drawArc(
            color = Color.Red,
            startAngle = 0f,
            sweepAngle = 360f,
            topLeft = Offset.Zero,
            useCenter = false,
            style = Stroke(
                30.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, first.value, 50f), 0f)
            )
        )
    }


}
@Composable
fun RotateTransition(modifier: Modifier) {
    val updates = rememberInfiniteTransition(label = "")

    val rotate = updates.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    Canvas(
        modifier = Modifier.size(120.dp)
            .padding(24.dp)
    ) {
        rotate(
            degrees = rotate.value,
            pivot = Offset(size.width / 2f, size.height / 2f)
        ){
            drawArc(
                brush = Brush.linearGradient(
                    listOf(
                        Color(90, 87, 90, 252),
                        Color(194, 191, 194, 252),
                    )
                ),
                startAngle = 0f,
                sweepAngle = 360f,
                topLeft = Offset(0f, 0f),
                useCenter = false,
                style = Stroke(
                    30.dp.toPx() ,
                    cap = StrokeCap.Butt,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 35f , 50f), 0f)
                )
            )

        }
    }
}

@Preview
@Composable
private fun ScreenLoadersPrev() {
    ScreenLoaders(modifier = Modifier.fillMaxSize())
}

