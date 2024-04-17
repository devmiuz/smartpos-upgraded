package uz.uzkassa.smartpos.core.presentation.utils.text

class RegexOutfitFilter {
    companion object {
        fun removeZeros(number: String): String {
            return if (number.contains(".")) number.replace("0*$".toRegex(), "")
                .replace("\\.$".toRegex(), "") else number
        }
    }
}