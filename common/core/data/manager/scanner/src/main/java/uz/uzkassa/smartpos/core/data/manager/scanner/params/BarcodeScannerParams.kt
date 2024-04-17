package uz.uzkassa.smartpos.core.data.manager.scanner.params

import android.view.KeyEvent

data class BarcodeScannerParams internal constructor(
    val vendor: String,
    val value: Any,
    val isMarking: Boolean
) {

    companion object {

        fun fromKeyEvent(
            event: KeyEvent?,
            isMarking: Boolean
        ): BarcodeScannerParams? = when (event?.action) {
            KeyEvent.ACTION_DOWN -> BarcodeScannerParams(
                vendor = event.device.name,
                value = event.unicodeChar.toChar(),
                isMarking = isMarking
            )
            else -> null
        }
    }
}