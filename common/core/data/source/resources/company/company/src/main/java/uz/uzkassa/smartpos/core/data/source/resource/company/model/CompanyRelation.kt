package uz.uzkassa.smartpos.core.data.source.resource.company.model

import androidx.room.Embedded
import androidx.room.Relation
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.city.model.CityEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.region.model.RegionEntity

data class CompanyRelation(
    @Embedded
    val cityEntity: CityEntity,

    @Embedded
    val companyBusinessTypeEntity: CompanyBusinessTypeEntity,

    @Embedded
    val companyEntity: CompanyEntity,

    @Embedded
    val regionEntity: RegionEntity,

    @Relation(parentColumn = "company_activity_types_ids", entityColumn = "activity_type_id")
    val activityTypeEntities: List<ActivityTypeEntity>
)