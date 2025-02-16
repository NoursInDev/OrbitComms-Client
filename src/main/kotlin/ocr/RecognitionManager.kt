package ocr

import net.sourceforge.tess4j.Tesseract
import utils.log
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.Window
import java.awt.image.BufferedImage
import java.awt.image.RescaleOp

class RecognitionManager() : IRecognitionManager {

    data class PlayerInfo(
        var CamDir: Array<Double>,
        var Coords: Array<Double>,
        var Shard: String,
    )

    private var window: Window? = null
    private var content: String? = null
    private var screenScope: Array<Int>
    private val tesseract: Tesseract
    private val playerInfo: PlayerInfo = PlayerInfo(arrayOf(0.0, 0.0, 0.0), arrayOf(0.0, 0.0, 0.0), "")

    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val datascope = arrayOf(615, 330) // 330 loses bottom data when too many zones (not a pb)
        screenScope = arrayOf(screenSize.width - datascope[0], 0, datascope[0], datascope[1])

        tesseract = Tesseract()
        tesseract.setDatapath("src/main/resources/tessdata")

        captureScreen()

        Thread {
            while (true) {
                captureScreen()
                Thread.sleep(1000 / 60)
            }
        }.start()
    }

    public constructor(screenScope: Array<Int>) : this() {
        this.screenScope = screenScope
    }

    override fun setScope(scope: Array<Int>) {
        screenScope = scope
    }

    override fun getScope(): Array<Int> = screenScope

    override fun getShard(): String? {
        playerInfo.Shard = content?.lines()?.find { it.startsWith("Authority Server:") } ?: return playerInfo.Shard
        return playerInfo.Shard
    }

    override fun getCoordinates(): Array<Double>? {
        val coos = content?.lines()?.find { it.startsWith("Zone: Root Pos:") } // todo not viable, to base on Zone: pryo/stanton(number)

        val coordsLine = coos?.substringAfter("Pos:")?.trim()
        log(coordsLine?: "No coordinates found")
        val coords = coordsLine?.split(" ")?.take(3)?.mapNotNull { it.dropLast(2).toDoubleOrNull() }?.toTypedArray()
        playerInfo.Coords = coords ?: playerInfo.Coords

        return playerInfo.Coords
    }

    override fun getFacing(): Array<Double>? {
        return null //not implemented yet
    }

    override fun setWindow(window: Window?) {
        this.window = window
    }

    private fun captureScreen(screenScope: Array<Int> = this.screenScope) {
        val robot = Robot()
        val screenRect = Rectangle(screenScope[0], screenScope[1], screenScope[2], screenScope[3])
        val capturedImage = robot.createScreenCapture(screenRect)

        val ocrResult = tesseract.doOCR(capturedImage)
        if (assertOCRValidResult(ocrResult)) content = ocrResult
    }

    private fun assertOCRValidResult(result : String) : Boolean {
        val lines = result.lines() ?: return false
        val hasZone = lines.any { it.startsWith("Zone:") }
        val hasAuthorityServer = lines.any { it.startsWith("Authority Server:") }

        if (!hasZone || !hasAuthorityServer) {
            return false
        }
        return true
    }
}