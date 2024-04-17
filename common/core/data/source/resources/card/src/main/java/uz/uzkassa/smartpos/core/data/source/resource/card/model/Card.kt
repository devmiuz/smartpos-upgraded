package uz.uzkassa.smartpos.core.data.source.resource.card.model

import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype.CardType
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.Customer
import java.math.BigDecimal
import java.util.*

data class Card(
    val id: Long,
    val activatedByUserId: Long?,
    val createdById: Long,
    val updatedById: Long?,
    val issuedByBranchId: Long?,
    val isActivated: Boolean,
    val isBlocked: Boolean,
    val balance: BigDecimal,
    val totalDiscount: BigDecimal,
    val cardNumber: String,
    val cardType: CardType,
    val customer: Customer?,
    val activatedDate: Date?,
    val createdDate: Date,
    val updatedDate: Date?
)