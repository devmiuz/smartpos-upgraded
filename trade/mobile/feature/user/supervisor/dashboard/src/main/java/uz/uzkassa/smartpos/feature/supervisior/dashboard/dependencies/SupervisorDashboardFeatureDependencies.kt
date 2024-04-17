package uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.dao.ActivityTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.dao.SalesDynamicsEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.service.SalesDynamicsRestService
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.data.source.resource.city.dao.CityEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.dao.CompanyBusinessTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.data.source.resource.region.dao.RegionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.dao.ReceiptTotalDao
import uz.uzkassa.smartpos.feature.sync.common.dependencies.SyncDependencies

interface SupervisorDashboardFeatureDependencies : SyncDependencies {

    val activityTypeEntityDao: ActivityTypeEntityDao

    override val categoryEntityDao: CategoryEntityDao

    override val categoryRestService: CategoryRestService

    val cityEntityDao: CityEntityDao

    val companyBusinessTypeEntityDao: CompanyBusinessTypeEntityDao

    val coroutineContextManager: CoroutineContextManager

    override val productEntityDao: ProductEntityDao

    val productUnitEntityDao: ProductUnitEntityDao

    override val productRestService: ProductRestService

    val receiptRestService: ReceiptRestService

    val receiptTotalDao: ReceiptTotalDao

    val regionEntityDao: RegionEntityDao

    val salesDynamicsEntityDao: SalesDynamicsEntityDao

    val salesDynamicsRestService: SalesDynamicsRestService

    val supervisorDashboardFeatureArgs: SupervisorDashboardFeatureArgs

    val supervisorDashboardFeatureCallback: SupervisorDashboardFeatureCallback

    val unitEntityDao: UnitEntityDao

    val unitRestService: UnitRestService

    val userAuthRestService: UserAuthRestService

    val userRelationDao: UserRelationDao
}