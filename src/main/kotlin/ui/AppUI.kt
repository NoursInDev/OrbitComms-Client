package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import core.AppManager
import kotlinx.coroutines.delay

@Composable
@Preview
fun App(main : AppManager = AppManager.instance) {
    AppTheme {
        var shardText by remember { mutableStateOf(main.ocr.getShard()) }
        var coordText by remember { mutableStateOf(main.ocr.getCoordinates()?.contentToString()) }

        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = shardText ?: "No text detected")
                Text(text = coordText ?: "No coordinates detected")

                LaunchedEffect(Unit) {
                    while (true) {
                        shardText = main.ocr.getShard()
                        coordText = main.ocr.getCoordinates()?.contentToString()
                        delay(1000L)
                    }
                }
            }
        }
    }
}