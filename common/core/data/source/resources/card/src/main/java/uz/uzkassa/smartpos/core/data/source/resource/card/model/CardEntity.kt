package uz.uzkassa.smartpos.core.data.source.resource.card.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.*

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey
    @ColumnInfo(name = "card_id")
    val id: Long,

    @ColumnInfo(name = "card_card_type_id")
    val cardTypeId: Long,

    @ColumnInfo(name = "card_customer_id")
    val customerId: Long?,

    @ColumnInfo(name = "card_activated_by_user_id")
    val activatedByUserId: Long?,

    @ColumnInfo(name = "card_created_by_id")
    val createdById: Long,

    @ColumnInfo(name = "card_updated_by_id")
    val updatedById: Long?,

    @ColumnInfo(name = "card_issued_by_branch_id")
    val issuedByBranchId: Long?,

    @ColumnInfo(name = "card_is_activated")
    val isActivated: Boolean,

    @ColumnInfo(name = "card_is_blocked")
    val isBlocked: Boolean,

    @ColumnInfo(name = "card_current_balance")
    val balance: BigDecimal,

    @ColumnInfo(name = "card_total_discount")
    val totalDiscount: BigDecimal,

    @ColumnInfo(name = "card_number")
    val cardNumber: String,

    @ColumnInfo(name = "card_activated_date")
    val activatedDate: Date?,

    @ColumnInfo(name = "card_created_date")
    val createdDate: Date,

    @ColumnInfo(name = "card_updated_date")
    val updatedDate: Date?
)