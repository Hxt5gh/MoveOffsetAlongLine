package com.hxt5gh.movealongthepath.Animation

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.random.Random


@Composable
fun Gravity() {
    val game = remember { GameGravity() }

    game.start()

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                Log.d("withFrameNanos", "MovingToTheWidth: ${it} ")
                game.update(it)
            }
        }
    }
    Canvas(modifier = Modifier
        .fillMaxSize()
        .fillMaxHeight(.5f)
        .background(Color.Black)
        ) {
        (game.totalTime / 1E8).roundToInt() / 20f
        game.width = size.width
        game.height = size.height
        game.gameObjects.forEach {
            when (it) {
                is GravityObject -> {
                    drawCircle(
                        radius =it.radius,
                        center = Offset(it.xOffset, it.yOffset),
                        color = it.color
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true  , showSystemUi = true)
@Composable
fun GravityPrev() {
    Gravity()
}

val colorList = listOf(
    Color(0xFFFF7F3E),
    Color(0xFFFFF6E9),
    Color(0xFF80C4E9),
    Color(0xFF604CC3)
)

class GameGravity {
    var prevTime = 0L
    var width by mutableStateOf(0.dp.value)
    var height by mutableStateOf(0.dp.value)

    var gameObjects = mutableStateListOf<GravityObject>()
    var totalTime by mutableStateOf(0L)


    fun start() {
        gameObjects.clear()
        repeat(50) {
            val ball = GravityObject(
                xOffset = Random.nextFloat() * width,
                yOffset = Random.nextFloat() * height/4,
                xVelocity = 40f,
                yVelocity = 80f,  //velocity added every frame
                gravity = (Random.nextFloat() * 80f).coerceIn(40f , 80f), // velocity added to yVelocity every frame
                friction = 0.95f, // friction for decreasing velocity when hit he ground
                color = colorList.random(),
                radius = (Random.nextFloat() *  50).coerceIn(20f , 40f)
            )

            gameObjects.add(ball)
        }
    }

    fun update(time: Long) {
        val delta = time - prevTime
        val floatDelta = (delta / 1E8).toFloat()
        prevTime = time

        for (gameObject in gameObjects) {
            gameObject.update(floatDelta , this)
        }

        totalTime += delta
    }
}

class GravityObject(
    var xOffset: Float,
    var yOffset: Float,
    var xVelocity: Float,
    var yVelocity: Float,
    var gravity: Float,
    var friction : Float,
    var color: Color,
    var radius: Float,
){


    fun update(realDelta: Float, game: GameGravity){
        if (this.yOffset + this.radius + this.yVelocity > game.height){
            this.yVelocity = -this.yVelocity * this.friction
        }
        else {
            this.yVelocity += gravity * realDelta
            Log.d("gravity", "update: ${this.yOffset}")
        }

        if (this.xOffset + radius > game.width || this.xOffset  - radius < 0){
            this.xVelocity = -this.xVelocity
        }

        this.yOffset += yVelocity * realDelta
        this.xOffset += xVelocity * realDelta
    }
}