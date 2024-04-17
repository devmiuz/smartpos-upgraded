package uz.uzkassa.smartpos.core.data.source.resource.customer.model

import uz.uzkassa.smartpos.core.data.source.resource.customer.model.gender.CustomerGender
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.type.CustomerType
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullName
import java.util.*

data class Customer(
    val id: Long,
    val createdById: Long,
    val updatedById: Long,
    val createdByBranchId: Long,
    val isBlocked: Boolean,
    val fullName: FullName,
    val gender: CustomerGender?,
    val customerType: CustomerType?,
    val address: String?,
    val dob: String?,
    val phone: String,
    val email: String?,
    val description: String?,
    val reasonForBlock: String?,
    val createdDate: Date?,
    val updatedDate: Date?
)