package uz.uzkassa.smartpos.feature.regioncity.selection.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.city.dao.CityEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.city.service.CityRestService
import uz.uzkassa.smartpos.core.data.source.resource.region.dao.RegionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.region.service.RegionRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface RegionCitySelectionFeatureDependencies {

    val cityEntityDao: CityEntityDao

    val cityRestService: CityRestService

    val coroutineContextManager: CoroutineContextManager

    val regionEntityDao: RegionEntityDao

    val regionRestService: RegionRestService

    val regionCitySelectionFeatureArgs: RegionCitySelectionFeatureArgs

    val regionCitySelectionFeatureCallback: RegionCitySelectionFeatureCallback
}