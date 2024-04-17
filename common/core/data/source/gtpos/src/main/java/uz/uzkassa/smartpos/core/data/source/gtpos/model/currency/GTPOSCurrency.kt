package uz.uzkassa.smartpos.core.data.source.gtpos.model.currency

enum class GTPOSCurrency(internal val code: Int) {
    EUR(978),
    USD(840),
    UZS(860);

    internal companion object {

        fun valueOf(code: Int): GTPOSCurrency = when (code) {
            EUR.code -> EUR
            USD.code -> USD
            UZS.code -> UZS
            else -> throw UnsupportedOperationException("Unable to parse currency")
        }
    }
}