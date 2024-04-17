package uz.uzkassa.apay.data.repository

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.apay.data.model.ApayCreateBillResponse
import uz.uzkassa.apay.data.model.ApayUpdateBillResponse
import uz.uzkassa.apay.data.model.PayedResponse
import uz.uzkassa.apay.data.model.card_list.CardData
import uz.uzkassa.apay.data.model.card_list.CardListResponse
import uz.uzkassa.apay.data.model.pay_bill.PayBillParams
import uz.uzkassa.apay.data.model.pay_bill.PayBillResponse
import uz.uzkassa.apay.data.repository.params.CreateBillParams
import uz.uzkassa.apay.data.network.rest.ApayRestService
import uz.uzkassa.apay.data.repository.params.UpdateBillParams
import uz.uzkassa.smartpos.core.data.source.resource.apay.remote.socket.ApaySocketService
import javax.inject.Inject

internal class ApayPaymentRepositoryImpl @Inject constructor(
    private val apayRestService: ApayRestService,
    private val apaySocketService: ApaySocketService
) : ApayPaymentRepository {

    override fun createBill(bill: CreateBillParams, branchId: Long): Flow<ApayCreateBillResponse> {
        return apayRestService
            .createBill(branchId, bill.asJsonElement())
    }

    override fun updateBill(updateBillParams: UpdateBillParams): Flow<ApayUpdateBillResponse> {
        return apayRestService.updateBill(updateBillParams.asJsonElement())
    }

    override fun payBill(payBillParams: PayBillParams): Flow<PayBillResponse> {
        return apayRestService.payBill(payBillParams.asJsonElement())
    }

    override fun cardList(billId: String, nfc: String, pin: String): Flow<List<CardListResponse>> {
        return apayRestService.cardList(billId, nfc, pin)
    }

    override fun getCardBackground(cardNumber: String): Flow<CardData> {
        return apayRestService.getCardBackground(cardNumber)
    }

    override fun listenApaySocketService(): Flow<String> {
        return apaySocketService.onApayDataReceived()
    }

    override fun checkPayUzcard(billId: String): Flow<PayedResponse> {
        return apayRestService.checkPayUzcard(billId)
    }

}