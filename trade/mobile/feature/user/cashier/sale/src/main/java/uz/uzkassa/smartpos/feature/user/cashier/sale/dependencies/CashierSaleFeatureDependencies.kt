package uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies

import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.manager.printer.PrinterManager
import uz.uzkassa.smartpos.core.data.manager.scanner.BarcodeScannerManager
import uz.uzkassa.smartpos.core.data.source.fiscal.source.receipt.FiscalReceiptSource
import uz.uzkassa.smartpos.core.data.source.fiscal.source.shift.FiscalShiftSource
import uz.uzkassa.smartpos.core.data.source.gtpos.intent.GTPOSLaunchIntent
import uz.uzkassa.smartpos.core.data.source.gtpos.jetpack.GTPOSJetpackComponent
import uz.uzkassa.smartpos.core.data.source.gtpos.source.batch.GTPOSBatchSource
import uz.uzkassa.smartpos.core.data.source.gtpos.source.payment.GTPOSPaymentSource
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.dao.ActivityTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.data.source.resource.city.dao.CityEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.dao.CompanyBusinessTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.service.CompanyRestService
import uz.uzkassa.smartpos.core.data.source.resource.marking.dao.ProductMarkingDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductPaginationRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.dao.ProductPackageTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.payment.ReceiptPaymentEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.dao.ReceiptDraftEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.dao.ReceiptDraftRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.service.ReceiptTemplateRestService
import uz.uzkassa.smartpos.core.data.source.resource.region.dao.RegionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.region.service.RegionRestService
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.service.ShiftReportRestService
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.service.UserRoleRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.sync.common.dependencies.SyncDependencies

interface CashierSaleFeatureDependencies : SyncDependencies {

    val activityTypeEntityDao: ActivityTypeEntityDao

    val barcodeScannerManager: BarcodeScannerManager

    val branchEntityDao: BranchEntityDao

    val branchRelationDao: BranchRelationDao

    val branchRestService: BranchRestService

    override val categoryEntityDao: CategoryEntityDao

    override val categoryRestService: CategoryRestService

    val cashierSaleFeatureArgs: CashierSaleFeatureArgs

    val cashierSaleFeatureCallback: CashierSaleFeatureCallback

    val cashTransactionEntityDao: CashTransactionEntityDao

    val cityEntityDao: CityEntityDao

    val companyBusinessTypeEntityDao: CompanyBusinessTypeEntityDao

    val companyEntityDao: CompanyEntityDao

    val companyRelationDao: CompanyRelationDao

    val companyRestService: CompanyRestService

    val coroutineContextManager: CoroutineContextManager

    val deviceInfoManager: DeviceInfoManager

    val gtposBatchSource: GTPOSBatchSource

    val gtposLaunchIntent: GTPOSLaunchIntent

    val gtposJetpackComponent: GTPOSJetpackComponent

    val gtposPaymentSource: GTPOSPaymentSource

    val fiscalReceiptSource: FiscalReceiptSource

    val fiscalShiftSource: FiscalShiftSource

    val printerManager: PrinterManager

    override val productEntityDao: ProductEntityDao

    val productMarkingDao: ProductMarkingDao

    val productPackageTypeEntityDao: ProductPackageTypeEntityDao

    val productPaginationRelationDao: ProductPaginationRelationDao

    val productRelationDao: ProductRelationDao

    override val productRestService: ProductRestService

    val productUnitEntityDao: ProductUnitEntityDao

    val regionEntityDao: RegionEntityDao

    val regionRestService: RegionRestService

    val receiptDetailEntityDao: ReceiptDetailEntityDao

    val receiptDraftRelationDao: ReceiptDraftRelationDao

    val receiptDraftEntityDao: ReceiptDraftEntityDao

    val receiptEntityDao: ReceiptEntityDao

    val receiptPaymentEntityDao: ReceiptPaymentEntityDao

    val receiptRestService: ReceiptRestService

    val receiptTemplateEntityDao: ReceiptTemplateEntityDao

    val receiptTemplateRestService: ReceiptTemplateRestService

    val shiftReportEntityDao: ShiftReportEntityDao

    val shiftReportRestService: ShiftReportRestService

    val unitEntityDao: UnitEntityDao

    val unitRestService: UnitRestService

    val userAuthRestService: UserAuthRestService

    val userEntityDao: UserEntityDao

    val userRelationDao: UserRelationDao

    val userRestService: UserRestService

    val userRoleEntity: UserRoleEntityDao

    val userRoleRestService: UserRoleRestService

    val receiptRelationDao: ReceiptRelationDao
}