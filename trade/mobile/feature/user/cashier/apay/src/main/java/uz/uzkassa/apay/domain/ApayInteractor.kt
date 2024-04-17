package uz.uzkassa.apay.domain

import android.util.Log
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.uzkassa.apay.data.model.ApayBillDetail
import uz.uzkassa.apay.data.model.ApayUpdateBillResponse
import uz.uzkassa.apay.data.model.PayedResponse
import uz.uzkassa.apay.data.model.card_list.CardData
import uz.uzkassa.apay.data.model.card_list.CardListResponse
import uz.uzkassa.apay.data.model.pay_bill.PayBillParams
import uz.uzkassa.apay.data.model.pay_bill.PayBillResponse
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.coroutines.flow.flowInterval
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import uz.uzkassa.apay.data.repository.ApayPaymentRepository
import uz.uzkassa.apay.data.repository.params.CreateBillParams
import uz.uzkassa.apay.data.repository.params.UpdateBillParams
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.amount.Amount
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class ApayInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val apayPaymentRepository: ApayPaymentRepository,
    private val currentBranchPreference: CurrentBranchPreference,
    private val args: CashierApayFeatureArgs
) {

    private var paidAmount = BigDecimal.ZERO

    fun getPaidAmount(): Amount {
        return Amount(
            amount = paidAmount,
            changeAmount = BigDecimal.ZERO,
            leftAmount = BigDecimal.ZERO,
            totalAmount = BigDecimal.ZERO,
            type = ReceiptPayment.Type.APAY,
            creditAdvanceHolder = args.creditAdvanceHolder
        )
    }

    fun createBill(amount: BigDecimal, uniqueId: String): Flow<Result<ApayBillDetail>> {
        paidAmount = amount
        return apayPaymentRepository
            .createBill(
                CreateBillParams(amount = amount, uniqueId =uniqueId),
                currentBranchPreference.branchId ?: 0L
            )
            .map {
                Log.wtf("Apay bill response", it.toString())
                it.billDetail
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun updateBill(
        billId: String,
        clientId: String? = null,
        expiry: String? = null,
        nfc: String? = null,
        pan: String? = null,
        phone: String? = null
    ): Flow<Result<ApayUpdateBillResponse>> {
        return apayPaymentRepository
            .updateBill(
                UpdateBillParams(
                    billId = billId,
                    clientId = clientId,
                    expiry = expiry,
                    nfc = nfc,
                    pan = pan,
                    phone = phone
                )
            )
            .map {
                Log.wtf("sss", it.toString())
                it
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }


    fun payBill(
        billId: String,
        cardId: String? = null,
        confirmationKey: String? = null
    ): Flow<Result<PayBillResponse>> {
        return apayPaymentRepository
            .payBill(
                PayBillParams(
                    billId = billId,
                    cardId = cardId,
                    confirmationKey = confirmationKey
                )
            )
            .map {
                Log.wtf("sss", it.toString())
                it
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun cardList(
        billId: String,
        nfc: String,
        pin: String
    ): Flow<Result<List<CardListResponse>>> {
        return apayPaymentRepository
            .cardList(
                billId, nfc, pin
            )
            .map {
                Log.wtf("sss", it.toString())
                it
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }


    fun getCardData(cardNumber: String): Flow<Result<CardData>> {
        return apayPaymentRepository
            .getCardBackground(cardNumber)
            .map {
                Log.wtf("sss", it.toString())
                it
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun listenApaySocketService(): Flow<String> {
        return apayPaymentRepository.listenApaySocketService()
    }

    @ObsoleteCoroutinesApi
    fun startCountdownTimer(): Flow<Long> {
        return flowInterval(COUNTDOWN_TIMER_AMOUNT, 1, TimeUnit.SECONDS)
            .flowOn(coroutineContextManager.ioContext)
    }

    companion object {
        const val COUNTDOWN_TIMER_AMOUNT = 60L; // IN SECONDS
    }


    fun checkPayUzcard(billId: String): Flow<Result<PayedResponse>> {
        return apayPaymentRepository.checkPayUzcard(billId).map { it }.flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

}