package com.hxt5gh.movealongthepath.Animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hxt5gh.movealongthepath.R
import kotlinx.coroutines.launch
import kotlin.math.abs


data class DatingData(
    val name: String,
    val image: Int,
    val age: Int,
    val location: String,
    val title: String,
    val resion: String
)

var dataList = mutableStateListOf(
    DatingData(
        "Hulk",
        R.drawable.hulk,
        42,
        "Anywhere (when angry)",
        "Strongest There Is",
        resion = "Looking for My Calmer Half"
    ),
    DatingData(
        "Spiderman",
        R.drawable.spider,
        18,
        "New York City",
        "Friendly Neighborhood Spiderman",
        resion = "Swinging into Love"
    ),
    DatingData(
        "Black Widow",
        R.drawable.widow,
        35,
        "New York City",
        "Super Soldier and Spy",
        resion = "Seeking a Partner in Espionage"
    ),
    DatingData(
        "Captain America",
        R.drawable.cap,
        100,
        "Brooklyn",
        "Soldier",
        resion = "Building a Team of Two"
    ),
    DatingData(
        "Iron Man",
        R.drawable.ironman,
        50,
        "Malibu",
        "Billionaire, Playboy",
        resion = "Seeking a Partner in Crime"
    ),
    DatingData(
        "Thor",
        R.drawable.thor,
        1500,
        "Asgard",
        "God of Thunder",
        resion = "Looking for My Lady"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatingDrag(modifier: Modifier = Modifier) {


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Draggable UI") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .height(500.dp)
                        .widthIn(min = 300.dp),
                    contentAlignment = Alignment.Center
                ) {

                    if (!dataList.isEmpty()) {
                        dataList.forEachIndexed { index, datingData ->
                            CustomCard(
                                datingData,
                                index,
                                onRemove = {
                                    dataList.remove(datingData)
                                })
                        }
                    } else {
                        Text(text = "No Data Found")
                    }
                }
            }

        }
    }
}

@Composable
fun CustomCard(data: DatingData, index: Int, onRemove: () -> Unit) {
    var offsetX = remember {
        Animatable(0f, Float.VectorConverter)
    }
    var offsetY = remember {
        Animatable(0f, Float.VectorConverter)
    }
    val scope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val swipeLX = -screenWidth.value * 2.5f
    val swipeRX = screenWidth.value * 2.5f
    val swipeTY = -1000f
    val swipeBY = 1000f
    offsetX.updateBounds(swipeLX, swipeRX)
    offsetY.updateBounds(swipeTY, swipeBY)

    if (abs(offsetX.value) < swipeRX) {
        val rotation = (offsetX.value / 45)
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .height(550.dp)
                .padding(top = 8.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            scope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount.x)
                                offsetY.snapTo(offsetY.value + dragAmount.y)
                            }

                        },
                        onDragEnd = {
                            if (abs(offsetX.targetValue) < abs(swipeRX) / 2.5) {
                                scope.launch {
                                    offsetX.animateTo(0f, tween(400, easing = EaseOutElastic))
                                }
                                scope.launch {
                                    offsetY.animateTo(0f, tween(400, easing = EaseOutElastic))
                                }
                            } else {
                                scope.launch {
                                    if (offsetX.value > 0) {
                                        offsetX.animateTo(
                                            swipeRX,
                                            tween(400, easing = EaseOutElastic)
                                        )
                                    } else {
                                        offsetX.animateTo(
                                            -swipeRX,
                                            tween(400, easing = EaseOutElastic)
                                        )
                                    }
                                }
                            }

                        }
                    )
                }
                .graphicsLayer(
                    rotationZ = rotation,
                    translationX = offsetX.value,
                    translationY = offsetY.value
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondary),
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        painter = painterResource(id = data.image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = data.name,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                text = data.title,
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                                maxLines = 1
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = "Age : " + data.age.toString(),
                                fontSize = 16.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = data.location, fontSize = 16.sp, color = Color.DarkGray)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            text = data.resion,
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            maxLines = 1
                        )
                    }
                }

            }
        }
    } else {
        //remove from the list here
        onRemove()
    }
}

@Preview(showBackground = true)
@Composable
fun DatingDragPrev(modifier: Modifier = Modifier) {
    DatingDrag(modifier = Modifier.fillMaxSize())
}
