package com.hxt5gh.movealongthepath.Animation

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInExpo
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hxt5gh.movealongthepath.R
import kotlinx.coroutines.launch




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyColumnAnimation(modifier: Modifier = Modifier.fillMaxSize()) {

    var selectedIndex by remember {
        mutableStateOf(-1)
    }
    val chipList = listOf("Rotate+alpha",  "rotateX" , "Swipe Left" ,"Alpha" , "Size")
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "LazyColumn Animation") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) {
        Surface(modifier = Modifier
            .padding(it)
            .background(MaterialTheme.colorScheme.onErrorContainer)) {
            Column(modifier = modifier) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .background(MaterialTheme.colorScheme.surface),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {
                    itemsIndexed(chipList){ index, item ->
                        FilterChip(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            selected = selectedIndex == index,
                            onClick = {
                                if (selectedIndex == index){
                                    selectedIndex = -1
                                    return@FilterChip
                                }
                                selectedIndex = index
                            },
                            label = { Text(text = item) },
                          colors = FilterChipDefaults.filterChipColors(
                              containerColor = MaterialTheme.colorScheme.surface
                          )
                        )
                    }
                }

                LazyColumn(
                    modifier = modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    items(200) {
                        itemInner(it = it , selectedIndex = selectedIndex)
                    }
                }
            }

        }
    }
}

data class Data(
    val image : Int ,
    val name : String
)

val image = listOf(
     Data(R.drawable.cap, "Captain America"),
     Data(R.drawable.thor , "Thor"),
     Data(R.drawable.venom , "Venom") ,
     Data(R.drawable.hulk , "Hulk"),
     Data(R.drawable.vison , "Vision"),
     Data(R.drawable.hawkeye , "Hawkeye"),
     Data(R.drawable.ironman , "Ironman"),
     Data(R.drawable.spiderman , "Spiderman")
)

@Composable
fun itemInner(it: Int, selectedIndex: Int) {
    val scope = rememberCoroutineScope()
    val customModifier = when (selectedIndex) {
        -1 -> {
            Modifier.height(60.dp)
        }
        0 -> {
            val rotationValue = remember { Animatable(initialValue = 0f) }
            val alphaValue = remember { Animatable(initialValue = 0f) }

            LaunchedEffect(key1 = selectedIndex) {
                launch {
                    rotationValue.animateTo(
                        targetValue = 360f,
                        animationSpec = tween(300)
                    )
                }
                launch {
                    alphaValue.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(200, easing = FastOutSlowInEasing)
                    )
                }
            }
            Modifier
                .height(60.dp)
                .alpha(alphaValue.value)
                .graphicsLayer {
                    transformOrigin = TransformOrigin(1f, 0f)
                    rotationX = rotationValue.value
                }


        }
        1 ->{
            val animatedProgress = remember { Animatable(initialValue = 0f) }
            LaunchedEffect(Unit) {
                animatedProgress.animateTo(
                    targetValue = 360f,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            }
            Modifier
                .padding(8.dp)
                .graphicsLayer(rotationX = animatedProgress.value)
        }
        2 -> {

            val slideValue = remember { Animatable(initialValue = 200.dp.value) }
            LaunchedEffect(key1 = selectedIndex) {

                slideValue.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(200 , easing = FastOutSlowInEasing)
                )

            }
            Modifier
                .height(60.dp)
                .graphicsLayer {
                    translationX = slideValue.value
                }
        }
        3 -> {

            val alphaValue = remember { Animatable(initialValue = 0f) }
            val sizeValue = remember { Animatable(initialValue = 1.5f) }
            LaunchedEffect(key1 = selectedIndex) {

                launch {
                alphaValue.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(600 , easing = EaseInBounce)
                )
                }
                launch {
                    sizeValue.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(600 , easing = EaseInBounce)
                    )
                }

            }
            Modifier
                .height(60.dp)
                .graphicsLayer {
                    alpha = alphaValue.value
                }
        }

        4 ->{
            val sizeValue = remember { Animatable(initialValue = 0.0f) }
            LaunchedEffect(Unit) {
                sizeValue.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(300, easing = EaseIn)
                )
            }
            Modifier
                .padding(8.dp)
                .graphicsLayer(scaleY = sizeValue.value, scaleX = sizeValue.value)
        }

        else -> Modifier
    }


    Divider(modifier = Modifier.padding(horizontal = 8.dp))
    Card(
        colors = CardDefaults.cardColors( MaterialTheme.colorScheme.surface),
        modifier = customModifier

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            val image = image.random()
            Image(
                painter = painterResource(id = image.image),
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp)
                    .clip(
                        RoundedCornerShape(100.dp)
                    ),
                contentScale = ContentScale.Crop
            )
            Text(
                text = image.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                fontSize = 28.sp,
            )


        }
    }
    Spacer(modifier = Modifier.height(4.dp))

}



@Preview(showBackground = true , showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun prev() {
    LazyColumnAnimation(modifier = Modifier.fillMaxSize())
}

