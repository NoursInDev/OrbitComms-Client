package core

import ocr.IRecognitionManager
import ocr.RecognitionManager
import utils.log

class AppManager(
    val ocr: RecognitionManager
) {
    init {
        log("AppManager initialized")
    }

    companion object {
        val instance by lazy {
            AppManager(RecognitionManager())
        }
    }
}