package uz.uzkassa.smartpos.core.data.source.resource.branch.model.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchResponse

@Serializable
data class BranchPaginationResponse(
    @SerialName("content")
    val branches: List<BranchResponse>,

    @SerialName("last")
    val isLast: Boolean,

    @SerialName("size")
    val size: Int
)