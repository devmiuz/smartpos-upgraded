package uz.uzkassa.smartpos.feature.launcher.dependencies

import uz.uzkassa.smartpos.core.data.source.preference.cleaner.PreferenceCleaner
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.dao.ActivityTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.service.ActivityTypeRestService
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.data.source.resource.city.dao.CityEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.dao.CompanyBusinessTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.service.CompanyBusinessTypeRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.service.CompanyRestService
import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.service.ReceiptTemplateRestService
import uz.uzkassa.smartpos.core.data.source.resource.region.dao.RegionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.service.UserRoleRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import uz.uzkassa.smartpos.feature.launcher.data.preference.state.LauncherStatePreference
import uz.uzkassa.smartpos.feature.sync.common.dependencies.SyncDependencies

interface LauncherFeatureDependencies : SyncDependencies {

    val activityTypeEntityDao: ActivityTypeEntityDao

    val activityTypeRestService: ActivityTypeRestService

    val branchEntityDao: BranchEntityDao

    val branchRelationDao: BranchRelationDao

    val branchRestService: BranchRestService

    override val categoryEntityDao: CategoryEntityDao

    override val categoryRestService: CategoryRestService

    val currentBranchPreference: CurrentBranchPreference

    val cityEntityDao: CityEntityDao

    val companyBusinessTypeEntityDao: CompanyBusinessTypeEntityDao

    val companyBusinessTypeRestService: CompanyBusinessTypeRestService

    val companyEntityDao: CompanyEntityDao

    val companyRelationDao: CompanyRelationDao

    val companyRestService: CompanyRestService

    val coroutineContextManager: CoroutineContextManager

    val languagePreference: LanguagePreference

    val launcherFeatureArgs: LauncherFeatureArgs

    val launcherFeatureCallback: LauncherFeatureCallback

    val launcherStatePreference: LauncherStatePreference

    override val productEntityDao: ProductEntityDao

    override val productRestService: ProductRestService

    val productUnitEntityDao: ProductUnitEntityDao

    val receiptTemplateEntityDao: ReceiptTemplateEntityDao

    val receiptTemplateRestService: ReceiptTemplateRestService

    val regionEntityDao: RegionEntityDao

    val userEntityDao: UserEntityDao

    val userRestService: UserRestService

    val unitEntityDao: UnitEntityDao

    val userRelationDao: UserRelationDao

    val unitRestService: UnitRestService

    val userRoleEntityDao: UserRoleEntityDao

    val userRoleRestService: UserRoleRestService

    val preferenceCleaner: PreferenceCleaner

    val receiptRelationDao: ReceiptRelationDao

    val receiptEntityDao: ReceiptEntityDao

    val receiptRestService: ReceiptRestService
}