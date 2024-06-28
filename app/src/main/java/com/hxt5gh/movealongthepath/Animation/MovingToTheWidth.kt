package com.hxt5gh.movealongthepath.Animation


import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun MovingToTheWidth() {
    val game = remember { Game() }

    game.start()

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                game.update(it)
            }
        }
    }
    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .padding(8.dp)) {
        (game.totalTime / 1E8).roundToInt() / 10f
        game.width = size.width
        game.height = size.height
        game.gameObjects.forEach {
            when (it) {
                is GameObject -> {
                    drawCircle(
                        radius =it.radius,
                        center = Offset(it.positionX, it.positionY),
                        color = it.color,
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MovingToTheWidthPrev(modifier: Modifier = Modifier) {
    MovingToTheWidth()
}

class Game {
    var prevTime = 0L
    var width by mutableStateOf(0.dp.value)
    var height by mutableStateOf(0.dp.value)

    var gameObjects = mutableStateListOf<GameObject>()
    var totalTime by mutableStateOf(0L)


    fun start() {
        gameObjects.clear()
        repeat(100) {
            val ball = GameObject(
                positionX = Random.nextFloat() * width,
                positionY = Random.nextFloat() * height,
                radius = Random.nextFloat() * 50f,
                color = Color(
                    Random.nextInt(256),
                    Random.nextInt(256),
                    Random.nextInt(256)
                ),
                randomDX = Random.nextFloat() * 60f
            )

            gameObjects.add(ball)
        }
    }

    fun update(time: Long) {
        val delta = time - prevTime
        val dt = (delta / 1E8).toFloat() //to Seconds
        prevTime = time

        for (gameObject in gameObjects) {
           // Log.d("delta", "update: ${dt} ")
            gameObject.update(dt , this)
        }

        totalTime += delta
    }
}

class GameObject(
    var positionX: Float,
    var positionY: Float,
    var radius: Float,
    var color: Color = Color.Red,
    val randomDX : Float
){
    var velocityX = randomDX.dp.value.coerceIn(10f , 60f)
    var velocityY = randomDX.dp.value.coerceIn(10f , 60f)
    fun update(realDelta: Float, game: Game){
        if (this.positionX + radius > game.width || this.positionX  - radius < 0){
            velocityX = -velocityX
        }
        if (this.positionY + radius > game.height || this.positionY - radius < 0){
            velocityY = -velocityY
        }
        this.positionX += velocityX * realDelta
        this.positionY += velocityY * realDelta
    }
}