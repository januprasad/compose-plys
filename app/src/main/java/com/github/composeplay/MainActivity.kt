package com.github.composeplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.composeplay.ui.theme.ComposePlayTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AnimatedBoxSize()
                }
            }
        }
    }
}

@Composable
fun AnimatedBoxSize() {
    var initialPadding by remember { mutableStateOf(10.dp) }
    val scope = rememberCoroutineScope()
    val animatablePadding = remember { Animatable(100f) }

    val buttonState by remember { mutableStateOf(ButtonState.IDLE) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .padding(top = animatablePadding.value.dp)
                .size(100.dp)
                .background(Color.Blue),
        )
        LaunchedEffect(key1 = true) {
            animatablePadding.animateTo(
                targetValue = if (buttonState == ButtonState.IDLE) 10f else 200f,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            )
        }
    }
}

enum class ButtonState {
    IDLE, PRESSED
}

@Composable
fun Greeting() {
    CustomSnackbarHostDemo()
}

@Composable
fun CustomSnackbarHost(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable (SnackbarHostState) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter,
    ) {
        content(snackbarHostState)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        ) { snackbarData ->
            CustomSnackbar(snackbarData = snackbarData)
        }
    }
}

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    slideInOffsetY: Float = 148f,
    slideOutOffsetY: Float = -148f,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 300),
) {
    var snackbarVisible by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = snackbarData) {
        snackbarVisible = true
        delay(3000L)
        snackbarVisible = false
    }

    val offsetY by animateFloatAsState(
        targetValue = if (snackbarVisible) 0f else slideInOffsetY,
        animationSpec = animationSpec,
    )

    if (snackbarVisible) {
        Box(
            modifier = modifier
                .offset(y = offsetY.dp)
                .padding(horizontal = 16.dp),
        ) {
            Snackbar(
                snackbarData = snackbarData,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
fun CustomSnackbarHostDemo() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentSize(),
    ) {
        Button(
            onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "This is a custom Snackbar",
                        actionLabel = "Dismiss",
                    )
                }
            },
        ) {
            Text("Show Custom Snackbar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomSnackbarHost(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.fillMaxSize(),
        ) { hostState ->
            SnackbarHost(
                hostState = hostState,
                modifier = Modifier.align(Alignment.End),
            ) {
                Snackbar(
                    snackbarData = it,
                    modifier = Modifier.align(Alignment.End),
                )
            }
        }
    }
}
