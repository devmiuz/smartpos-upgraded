package uz.uzkassa.smartpos.core.data.source.resource.customer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.gender.CustomerGenderResponse
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.type.CustomerTypeResponse
import uz.uzkassa.smartpos.core.data.source.resource.fullname.model.FullNameResponse
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.DateSerializer
import java.util.*

@Serializable
data class CustomerResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("createdById")
    val createdById: Long,

    @SerialName("updatedById")
    val updatedById: Long,

    @SerialName("createdByBranchId")
    val createdByBranchId: Long,

    @SerialName("blocked")
    val isBlocked: Boolean,

    @SerialName("fullName")
    val fullName: FullNameResponse,

    @SerialName("gender")
    val gender: CustomerGenderResponse? = null,

    @SerialName("customerType")
    val customerType: CustomerTypeResponse? = null,

    @SerialName("address")
    val address: String? = null,

    @SerialName("dob")
    val dob: String? = null,

    @SerialName("phone")
    val phone: String,

    @SerialName("email")
    val email: String?,

    @SerialName("description")
    val description: String? = null,

    @SerialName("reasonForBlock")
    val reasonForBlock: String? = null,

    @Serializable(with = DateSerializer.NotNullable::class)
    @SerialName("createdDateTime")
    val createdDate: Date,

    @Serializable(with = DateSerializer.Nullable::class)
    @SerialName("updatedDateTime")
    val updatedDate: Date? = null
)