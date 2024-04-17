package uz.uzkassa.smartpos.core.data.source.resource.customer.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.type.CustomerTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullNameEntity
import java.util.*

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey
    @ColumnInfo(name = "customer_id")
    val id: Long,

    @ColumnInfo(name = "customer_created_by_id")
    val createdById: Long,

    @ColumnInfo(name = "customer_updated_by_id")
    val updatedById: Long,

    @ColumnInfo(name = "customer_created_by_branch_id")
    val createdByBranchId: Long,

    @ColumnInfo(name = "customer_is_blocked")
    val isBlocked: Boolean,

    @Embedded(prefix = "customer_")
    val fullName: FullNameEntity,

    @ColumnInfo(name = "customer_gender")
    val gender: String?,

    @Embedded(prefix = "customer_")
    val customerType: CustomerTypeEntity?,

    @ColumnInfo(name = "customer_address")
    val address: String?,

    @ColumnInfo(name = "customer_dob")
    val dob: String?,

    @ColumnInfo(name = "customer_phone")
    val phone: String,

    @ColumnInfo(name = "customer_email")
    val email: String?,

    @ColumnInfo(name = "customer_description")
    val description: String?,

    @ColumnInfo(name = "customer_reason_for_block")
    val reasonForBlock: String?,

    @ColumnInfo(name = "customer_created_date")
    val createdDate: Date?,

    @ColumnInfo(name = "customer_updated_date")
    val updatedDate: Date?
)