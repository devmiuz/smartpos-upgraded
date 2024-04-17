package uz.uzkassa.smartpos.core.data.source.resource.product.model.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductResponse

@Serializable
data class ProductPaginationResponse(
    @SerialName("content")
    val products: List<ProductResponse>,

    @SerialName("last")
    val isLast: Boolean,

    @SerialName("totalPages")
    val totalPages: Int
)