package uz.uzkassa.smartpos.core.data.source.resource.card.model.cardclass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_classes")
data class CardClassEntity(
    @PrimaryKey
    @ColumnInfo(name = "card_class_type")
    val type: String,

    @ColumnInfo(name ="card_class_name_ru")
    val nameRu: String
)