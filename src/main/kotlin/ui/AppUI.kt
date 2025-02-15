package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toComposeImageBitmap
import core.AppManager
import kotlinx.coroutines.delay

@Composable
@Preview
fun App(main : AppManager = AppManager.instance) {
    AppTheme{
        var counter by remember { mutableStateOf(0) }

        var img by remember { mutableStateOf(main.ocr.captureScreen(0,0,1919,1080)) }

        Button(onClick = {
            counter++
        }) {
            Text("I've been clicked $counter times")
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawIntoCanvas {
                drawImage(img.toComposeImageBitmap())
            }
        }

        LaunchedEffect(Unit) {
            while (true) {
                img = main.ocr.captureScreen(0, 0, 1919, 1080)
                delay(2L)
            }
        }
    }
}