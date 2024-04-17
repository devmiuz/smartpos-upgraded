package uz.uzkassa.smartpos.feature.receipt.remote.dependencies

import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.dao.ProductPackageTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.detail.ReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.payment.ReceiptPaymentEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.dao.ReceiptDraftEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptAmountEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptDetailEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.old.dao.PostponeReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.ReceiptSocketService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface ReceiptRemoteFeatureDependencies {

    val coroutineContextManager: CoroutineContextManager

    val deviceInfoManager: DeviceInfoManager

    val receiptDraftEntityDao: ReceiptDraftEntityDao

    val receiptDetailEntityDao: ReceiptDetailEntityDao

    val receiptEntityDao: ReceiptEntityDao

    val receiptPaymentEntityDao: ReceiptPaymentEntityDao

    val receiptRemoteFeatureArgs: ReceiptRemoteFeatureArgs

//    val receiptSocketService: ReceiptSocketService
}