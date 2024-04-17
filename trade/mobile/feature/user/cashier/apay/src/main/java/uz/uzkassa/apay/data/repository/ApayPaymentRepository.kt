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
import uz.uzkassa.apay.data.repository.params.UpdateBillParams

internal interface ApayPaymentRepository {

    fun createBill(bill: CreateBillParams, branchId: Long): Flow<ApayCreateBillResponse>

    fun updateBill(updateBillParams: UpdateBillParams): Flow<ApayUpdateBillResponse>

    fun payBill(payBillParams: PayBillParams): Flow<PayBillResponse>

    fun cardList(billId: String, nfc: String, pin: String): Flow<List<CardListResponse>>

    fun getCardBackground(cardNumber: String): Flow<CardData>

    fun listenApaySocketService(): Flow<String>

    fun checkPayUzcard(billId: String): Flow<PayedResponse>
}