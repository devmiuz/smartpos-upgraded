package uz.uzkassa.smartpos.feature.check.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.company.service.CompanyRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.service.CashTransactionRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface ReceiptCheckFeatureDependencies {


    val receiptRelationDao: ReceiptRelationDao

    val receiptEntityDao: ReceiptEntityDao

    val companyRestService: CompanyRestService

    val userRestService: UserRestService

    val cashTransactionRestService: CashTransactionRestService

    val receiptRestService: ReceiptRestService

    val receiptCheckFeatureArgs: ReceiptCheckFeatureArgs

    val receiptCheckFeatureCallback: ReceiptCheckFeatureCallback

    val coroutineContextManager: CoroutineContextManager
}