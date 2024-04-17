package uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model

data class ProductPackageType(
    val id: Long,
    val checksum: Int,
    val isCountable: Boolean,
    val code: Long,
    val name: String,
    val nameRu: String,
    val nameUz: String,
    val description: String
)