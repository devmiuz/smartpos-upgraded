package uz.uzkassa.smartpos.core.data.source.resource.marking.model

data class ProductMarking(
    val id: Long = 0L,
    val productId: Long? = null,
    val marking: String = ""
)