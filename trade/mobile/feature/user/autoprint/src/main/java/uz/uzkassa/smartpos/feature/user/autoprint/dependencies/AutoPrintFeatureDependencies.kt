package uz.uzkassa.smartpos.feature.user.autoprint.dependencies

import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.manager.printer.PrinterManager
import uz.uzkassa.smartpos.core.data.source.fiscal.source.receipt.FiscalReceiptSource
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.payment.ReceiptPaymentEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface AutoPrintFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val autoPrintFeatureArgs: AutoPrintFeatureArgs

    val autoPrintFeatureCallback: AutoPrintFeatureCallback

    val deviceInfoManager: DeviceInfoManager

    val printerManager: PrinterManager

    val companyRelationDao: CompanyRelationDao

    val branchRelationDao: BranchRelationDao

    val cashTransactionEntityDao: CashTransactionEntityDao

    val fiscalReceiptSource: FiscalReceiptSource

    val receiptDetailEntityDao: ReceiptDetailEntityDao

    val receiptEntityDao: ReceiptEntityDao

    val receiptPaymentEntityDao: ReceiptPaymentEntityDao

    val receiptTemplateEntityDao: ReceiptTemplateEntityDao

    val shiftReportEntityDao: ShiftReportEntityDao

    val userRelationDao: UserRelationDao
}