package uz.uzkassa.smartpos.core.data.source.resource.customer.mapper

import uz.uzkassa.smartpos.core.data.source.resource.customer.model.Customer
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.CustomerEntity
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.CustomerResponse
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.gender.CustomerGender
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.type.CustomerType
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.type.CustomerTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.fullname.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.fullname.mapper.mapToEntity

fun List<CustomerResponse>.map() =
    map { it.map() }

fun List<CustomerResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun CustomerResponse.map() =
    Customer(
        id = id,
        createdById = createdById,
        updatedById = updatedById,
        createdByBranchId = createdByBranchId,
        isBlocked = isBlocked,
        fullName = fullName.map(),
        gender = gender?.let { CustomerGender.valueOf(it.name) },
        customerType = customerType?.let { with(it) { CustomerType(type, nameRu) } },
        address = address,
        dob = dob,
        phone = phone,
        email = email,
        description = description,
        reasonForBlock = reasonForBlock,
        createdDate = createdDate,
        updatedDate = updatedDate
    )

fun CustomerResponse.mapToEntity() =
    CustomerEntity(
        id = id,
        createdById = createdById,
        updatedById = updatedById,
        createdByBranchId = createdByBranchId,
        isBlocked = isBlocked,
        fullName = fullName.mapToEntity(),
        gender = gender?.name,
        customerType = customerType?.let { CustomerTypeEntity(it.type, it.nameRu) },
        address = address,
        dob = dob,
        phone = phone,
        email = email,
        description = description,
        reasonForBlock = reasonForBlock,
        createdDate = createdDate,
        updatedDate = updatedDate
    )