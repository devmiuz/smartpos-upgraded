package uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch

import androidx.room.Embedded
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityEntity
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionEntity

class BranchRelation(
    @Embedded
    val branchEntity: BranchEntity,

    @Embedded
    val activityTypeEntity: ActivityTypeEntity?,

    @Embedded
    val regionEntity: RegionEntity?,

    @Embedded
    val cityEntity: CityEntity?
)