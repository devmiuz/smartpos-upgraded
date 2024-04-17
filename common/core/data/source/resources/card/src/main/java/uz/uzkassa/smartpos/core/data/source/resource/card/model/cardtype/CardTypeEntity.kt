package uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardclass.CardClassEntity
import java.util.*

@Entity(tableName = "card_types")
data class CardTypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "card_type_id")
    val id: Long,

    @ColumnInfo(name = "card_type_created_by_id")
    val createdById: Long,

    @ColumnInfo(name = "card_type_updated_by_id")
    val updatedById: Long?,

    @ColumnInfo(name = "card_type_discount_percent")
    val discountPercent: Double,

    @Embedded(prefix = "card_type_")
    val cardClass: CardClassEntity,

    @ColumnInfo(name = "card_type_name")
    val name: String,

    @ColumnInfo(name = "card_type_description")
    val description: String?,

    @ColumnInfo(name = "card_type_created_date")
    val createdDate: Date,

    @ColumnInfo(name = "card_type_updated_date")
    val updatedDate: Date?
)