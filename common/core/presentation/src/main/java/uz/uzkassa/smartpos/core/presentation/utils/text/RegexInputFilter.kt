package uz.uzkassa.smartpos.core.presentation.utils.text

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class RegexInputFilter(private val pattern: Pattern) : InputFilter {

    constructor(pattern: String) : this(Pattern.compile(pattern))

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? = if (!pattern.matcher(dest.toString() + source.toString()).matches()) ""
    else null
}