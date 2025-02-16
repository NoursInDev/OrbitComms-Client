package ocr

import java.awt.Window

interface IRecognitionManager {
    fun getShard() : String?
    fun getCoordinates() : Array<Double>?
    fun getFacing() : Array<Double>?

    fun getScope() : Array<Int>
    fun setScope(scope : Array<Int>)

    fun setWindow(window: Window?)
}