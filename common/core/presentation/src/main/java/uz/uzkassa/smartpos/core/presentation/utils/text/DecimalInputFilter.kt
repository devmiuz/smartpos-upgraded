package uz.uzkassa.smartpos.core.presentation.utils.text

import android.text.InputFilter
import android.text.Spanned
import android.util.Log

class DecimalInputFilter(private val length: Int) : InputFilter {

    var isDecimal = false
    var decimalsLength = 0

    override fun filter(
        newValue: CharSequence,
        sourceStart: Int,
        sourceEnd: Int,
        oldValue: Spanned,
        replacedStartPosition: Int,
        replacedEndPosition: Int
    ): CharSequence? {

        Log.e("DecimalInputFilter", "/-----***********-----/")

        val newInput = newValue.toString().trim()
        val oldInput = oldValue.toString().trim()

        val isCharacterRemoved = replacedStartPosition != replacedEndPosition
        Log.e("DecimalInputFilter", "isCharacterRemoved $isCharacterRemoved")

        if (isCharacterRemoved) {
            Log.e("DecimalInputFilter", "Character removed")
            val isPointRemoved = oldInput[replacedStartPosition] == '.'
            if (isPointRemoved) {
                Log.e("DecimalInputFilter", "Point removed")
                isDecimal = false
                decimalsLength = 0
            } else {
                Log.e("DecimalInputFilter", "Number removed")
                if (isDecimal) decimalsLength -= 1
            }
        } else {
            Log.e("DecimalInputFilter", "Character entered")

            if (newInput.isNotEmpty()) {
                if (newInput == ".") {
                    Log.e("DecimalInputFilter", "Point entered")
                    if (replacedStartPosition != oldInput.length) return ""
                    isDecimal = true
                } else {
                    Log.e("DecimalInputFilter", "Number entered")
                    if (decimalsLength == length) return ""
                    val pointPosition = oldInput.indexOf(".")
                    if (replacedStartPosition > pointPosition) {
                        if (isDecimal) decimalsLength += 1
                    }
                }
            } else {
                Log.e("DecimalInputFilter", "Undefined symbol entered")
                return ""
            }
        }

        return null
    }
}