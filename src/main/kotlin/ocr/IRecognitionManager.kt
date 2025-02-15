package ocr

import java.awt.Window

interface IRecognitionManager {
    fun getShard() : String?
    fun getCoordinates() : Array<Double>?
    fun getFacing() : Array<Double>?

    fun setWindow(window: Window?)
}