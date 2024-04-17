package uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.mapper

import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model.ProductPackageType
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model.ProductPackageTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model.ProductPackageTypeResponse

fun List<ProductPackageTypeResponse>.map() =
    map { it.map() }

fun List<ProductPackageTypeResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun ProductPackageType.mapToResponse() =
    ProductPackageTypeResponse(
        id = id,
        code = code,
        checksum = checksum,
        isCountable = isCountable,
        name = name,
        nameRu = nameRu,
        nameUz = nameUz,
        description = description
    )

fun ProductPackageTypeEntity.map() =
    ProductPackageType(
        id = id,
        code = code,
        checksum = checksum,
        isCountable = isCountable,
        name = name,
        nameRu = nameRu,
        nameUz = nameUz,
        description = description
    )

fun ProductPackageTypeResponse.map() =
    ProductPackageType(
        id = id,
        code = code,
        checksum = checksum,
        isCountable = isCountable,
        name = name,
        nameRu = nameRu ?: name,
        nameUz = nameUz,
        description = description
    )

fun ProductPackageTypeResponse.mapToEntity() =
    ProductPackageTypeEntity(
        id = id,
        code = code,
        checksum = checksum,
        isCountable = isCountable,
        name = name,
        nameRu = nameRu ?: name,
        nameUz = nameUz,
        description = description
    )