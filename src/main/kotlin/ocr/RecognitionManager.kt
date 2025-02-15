package ocr

import java.awt.Rectangle
import java.awt.Robot
import java.awt.Window
import java.awt.image.BufferedImage

class RecognitionManager : IRecognitionManager {

    private var window: Window? = null

    override fun getShard(): String? {
        return null
    }

    override fun getCoordinates(): Array<Double>? {
        return null
    }

    override fun getFacing(): Array<Double>? {
        return null
    }

    override fun setWindow(window: Window?) {
        this.window = window
    }

    fun captureScreen(x: Int, y: Int, width: Int, height: Int): BufferedImage {
        // todo - change default screen capture
        val robot = Robot()
        val screenRect = Rectangle(x, y, width, height)
        return robot.createScreenCapture(screenRect)
    }
}