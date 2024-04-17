package uz.uzkassa.smartpos.core.data.source.resource.customer.mapper

import uz.uzkassa.smartpos.core.data.source.resource.customer.model.Customer
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.CustomerEntity
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.gender.CustomerGender
import uz.uzkassa.smartpos.core.data.source.resource.customer.model.type.CustomerType
import uz.uzkassa.smartpos.core.data.source.resource.fullname.mapper.map

fun List<CustomerEntity>.map() =
    map { it.map() }

fun CustomerEntity.map() =
    Customer(
        id = id,
        createdById = createdById,
        updatedById = updatedById,
        createdByBranchId = createdByBranchId,
        isBlocked = isBlocked,
        fullName = fullName.map(),
        gender = gender?.let { CustomerGender.valueOf(it) },
        customerType = customerType?.let { CustomerType(it.type, it.nameRu) },
        address = address,
        dob = dob,
        phone = phone,
        email = email,
        description = description,
        reasonForBlock = reasonForBlock,
        createdDate = createdDate,
        updatedDate = updatedDate
    )