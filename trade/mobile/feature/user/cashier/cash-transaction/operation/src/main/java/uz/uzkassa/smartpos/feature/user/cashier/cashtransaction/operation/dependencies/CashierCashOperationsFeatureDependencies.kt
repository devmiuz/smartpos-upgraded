package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies

import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.manager.printer.PrinterManager
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.dao.ShiftReportEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface CashierCashOperationsFeatureDependencies {

    val branchEntityDao: BranchEntityDao

    val branchRelationDao: BranchRelationDao

    val cashierCashOperationsFeatureArgs: CashierCashOperationsFeatureArgs

    val cashierCashOperationsFeatureCallback: CashierCashOperationsFeatureCallback

    val cashTransactionEntityDao: CashTransactionEntityDao

    val companyEntityDao: CompanyEntityDao

    val companyRelationDao: CompanyRelationDao

    val coroutineContextManager: CoroutineContextManager

    val deviceInfoManager: DeviceInfoManager

    val printerManager: PrinterManager

    val shiftReportEntityDao: ShiftReportEntityDao

    val userRelationDao: UserRelationDao

    val userEntityDao: UserEntityDao
}