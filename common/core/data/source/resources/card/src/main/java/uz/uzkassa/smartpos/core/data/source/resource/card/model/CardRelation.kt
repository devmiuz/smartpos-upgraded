package uz.uzkassa.smartpos.core.data.source.resource.card.model

import androidx.room.Embedded
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype.CardTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.CustomerEntity

data class CardRelation(
    @Embedded
    val cardEntity: CardEntity,

    @Embedded
    val cardTypeEntity: CardTypeEntity,

    @Embedded
    val customerEntity: CustomerEntity?
)