package uz.uzkassa.smartpos.core.data.source.gtpos.model.operation

import uz.uzkassa.smartpos.core.data.source.gtpos.model.currency.GTPOSCurrency
import uz.uzkassa.smartpos.core.utils.math.times
import java.math.BigDecimal

object GTPOSOperation {

    sealed class Request(open val operation: Type) {

        internal data class Batch(
            override val operation: Type
        ) : Request(operation)

        data class Payment(
            val rrn: Long?,
            val ticketNo: String?,
            val amount: BigDecimal?,
            val currency: GTPOSCurrency,
            override val operation: Type
        ) : Request(operation) {

            constructor(
                amount: BigDecimal,
                currency: GTPOSCurrency,
                operation: Type
            ) : this(null, null, amount * 100, currency, operation)

            constructor(
                rrn: Long?,
                amount: BigDecimal,
                currency: GTPOSCurrency,
                operation: Type
            ) : this(rrn, null, amount * 100, currency, operation)

            constructor(
                ticketNo: String?,
                amount: BigDecimal,
                currency: GTPOSCurrency,
                operation: Type
            ) : this(null, ticketNo, amount * 100, currency, operation)
        }
    }

    sealed class Response(open val operation: Type, open val status: Status) {

        data class Batch internal constructor(
            override val operation: Type,
            override val status: Status
        ) : Response(operation, status)

        data class Payment internal constructor(
            val amount: BigDecimal,
            val commissionAmount: BigDecimal?,
            val currency: GTPOSCurrency,
            override val operation: Type,
            override val status: Status
        ) : Response(operation, status)
    }

    enum class Status {
        ERROR,
        SUCCESS,
        WARNING
    }

    enum class Type {
        BALANCE,
        BATCH_CLOSE,
        BATCH_REPORT,
        COPY_RECEIPT,
        REFUND,
        REVERSAL,
        REVERSAL_LAST,
        SALE,
        UNSUPPORTED;
    }
}