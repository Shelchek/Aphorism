package com.example.aphorisms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aphorisms.model.Aphorism
import com.example.aphorisms.model.AphorismRep
import com.example.aphorisms.ui.theme.BunchOfAnimationsListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BunchOfAnimationsListTheme {
                ListOfAphorism()
            }
        }
    }
}

@Composable
fun ListOfAphorism() {
    LazyColumn {
        items(AphorismRep.aphList.shuffled()) {
            AphorismItem(aphorism = it)
        }
    }
}

@Composable
fun AphorismItem(aphorism: Aphorism, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(15.dp),
        elevation = 5.dp,
        backgroundColor = Color.LightGray

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
        ) {
            AuthorPicAndName(aphorism = aphorism)
            Spacer(modifier = Modifier.height(15.dp))
            AboutAndAffText(aphorism = aphorism)
        }
    }
}

@Composable
fun AuthorPicAndName(aphorism: Aphorism, modifier: Modifier = Modifier) {
    Card(
        elevation = 3.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var sizeMarker by remember { mutableStateOf(true) }
            var clipMarker by remember { mutableStateOf(true) }
            var rotateMarker by remember { mutableStateOf(true) }
            val sizeAnim by animateDpAsState(
                targetValue = if (sizeMarker) 70.dp else 400.dp,
                animationSpec = tween(durationMillis = 300)
            )
            val clipAnim by animateDpAsState(
                targetValue = if (clipMarker) 50.dp else 0.dp,
                animationSpec = tween(durationMillis = 700)
            )
            val rotateAnim by animateFloatAsState(
                targetValue = if (rotateMarker) 0F else 360F,
                animationSpec = tween(durationMillis = 1500)
            )
            Image(
                painter = painterResource(id = aphorism.authorPic),
                contentDescription = null,
                modifier = Modifier
                    .size(sizeAnim)
                    .clip(RoundedCornerShape(clipAnim))
                    .rotate(rotateAnim)
                    .graphicsLayer { rotationY = rotateAnim }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { clipMarker = !clipMarker },
                            onTap = { sizeMarker = !sizeMarker },
                            onLongPress = { rotateMarker = !rotateMarker }
                            )
                    },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = aphorism.authorName),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AboutAndAffText(aphorism: Aphorism, modifier: Modifier = Modifier) {
    Card(elevation = 3.dp) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize(
                    tween(
                        durationMillis = 200,
                        easing = LinearOutSlowInEasing
                    )
                )
        ) {
            var expandedAbout by remember { mutableStateOf(false) }
            var expandedAff by remember { mutableStateOf(false) }

            Row {
                Text(
                    text = "Об авторе:",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.weight(1F))
                IconExpand(expanded = expandedAbout) {
                    expandedAbout = !expandedAbout
                }
            }
            AnimatedContent(
                targetState = expandedAbout,
                transitionSpec = {
                    fadeIn(animationSpec = tween(1000)) with
                            fadeOut(animationSpec = tween(1000))
                }
            ) { expAb ->
                val string = stringResource(id = aphorism.authorAboutFull)
                Text(text = if (expAb) string else stringShortener(string))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.Bottom) {
                Row {
                    Text(text = "Изречение:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1F))
                    IconExpand(expanded = expandedAff) {
                        expandedAff = !expandedAff
                    }
                }
                AnimatedContent(
                    targetState = expandedAff,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(1000)) with
                                fadeOut(animationSpec = tween(1000))
                    }
                ) { expAff ->
                    val string = stringResource(id = aphorism.aphorism)
                    Text(text = if (expAff) string else stringShortener(string))
                }
            }
        }
    }
}


@Composable
fun IconExpand(expanded: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(35.dp)) {
        Icon(
            imageVector = if (!expanded) Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
            contentDescription = null
        )

    }
}

private fun stringShortener(string: String): String = string.substring(0, 35) + "..."