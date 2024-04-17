package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail

import androidx.room.Embedded
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryEntity
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.UnitEntity

data class ReceiptDetailRelation(
    @Embedded
    val categoryEntity: CategoryEntity?,

    @Embedded
    val receiptDetailEntity: ReceiptDetailEntity,

    @Embedded
    val unitEntity: UnitEntity?
)