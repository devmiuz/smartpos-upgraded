package uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies

import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.manager.printer.PrinterManager
import uz.uzkassa.smartpos.core.data.source.fiscal.source.receipt.FiscalReceiptSource
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.marking.dao.ProductMarkingDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface CashierRefundFeatureDependencies {

    val branchEntityDao: BranchEntityDao

    val branchRelationDao: BranchRelationDao

    val cashierRefundFeatureArgs: CashierRefundFeatureArgs

    val cashierRefundFeatureCallback: CashierRefundFeatureCallback

    val cashTransactionEntityDao: CashTransactionEntityDao

    val companyEntityDao: CompanyEntityDao

    val companyRelationDao: CompanyRelationDao

    val coroutineContextManager: CoroutineContextManager

    val deviceInfoManager: DeviceInfoManager

    val fiscalReceiptSource: FiscalReceiptSource

    val postponeReceiptEntityDao: PostponeReceiptEntityDao

    val printerManager: PrinterManager

    val productUnitEntityDao: ProductUnitEntityDao

    val receiptTemplateEntityDao: ReceiptTemplateEntityDao

    val receiptRestService: ReceiptRestService

    val receiptDetailEntityDao: ReceiptDetailEntityDao

    val receiptEntityDao: ReceiptEntityDao

    val receiptRelationDao: ReceiptRelationDao

    val shiftReportEntityDao: ShiftReportEntityDao

    val userEntityDao: UserEntityDao

    val userRelationDao: UserRelationDao

    val productMarkingDao: ProductMarkingDao
}
