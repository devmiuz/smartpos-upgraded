package uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region

data class Branch(
    val id: Long,
    val isDeleted: Boolean,
    val isFiscal: Boolean,
    val activityType: ActivityType?,
    val region: Region?,
    val city: City?,
    val name: String,
    val description: String?,
    val address: String?
)