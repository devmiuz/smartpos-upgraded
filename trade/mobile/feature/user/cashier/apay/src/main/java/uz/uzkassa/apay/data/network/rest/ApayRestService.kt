package uz.uzkassa.apay.data.network.rest

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.*
import uz.uzkassa.apay.data.model.ApayCreateBillResponse
import uz.uzkassa.apay.data.model.ApayUpdateBillResponse
import uz.uzkassa.apay.data.model.PayedResponse
import uz.uzkassa.apay.data.model.card_list.CardData
import uz.uzkassa.apay.data.model.card_list.CardListResponse
import uz.uzkassa.apay.data.model.pay_bill.PayBillResponse
import uz.uzkassa.apay.data.network.rest.ApayRestService.Companion.CHECK_PAY_UZCARD

interface ApayRestService {


    @POST(CREATE_BILL)
    fun createBill(
        @Header("BRANCH_ID") branchId: Long,
        @Body jsonElement: JsonElement
    ): Flow<ApayCreateBillResponse>

    @PUT(CREATE_BILL)
    fun updateBill(@Body jsonElement: JsonElement): Flow<ApayUpdateBillResponse>

    @PUT(PAY_BILL)
    fun payBill(@Body jsonElement: JsonElement): Flow<PayBillResponse>

    @GET(CARD_LIST)
    fun cardList(
        @Query("billId") billId: String,
        @Query("nfc") nfcdevda: String,
        @Query("pin") pin: String
    ): Flow<List<CardListResponse>>

    @GET(CARD_BACKGROUND)
    fun getCardBackground(@Query("pan") pan: String): Flow<CardData>

    @PUT(ATTACH_RECEIPT)
    fun attachReceipt(@Body jsonElement: JsonElement): Flow<Unit>

    @GET(CHECK_PAY_UZCARD)
    fun checkPayUzcard(@Path("billId") billId: String): Flow<PayedResponse>

    companion object {
        const val CREATE_BILL = "api/apay/bill"
        const val PAY_BILL = "api/apay/pay-bill"
        const val CARD_LIST = "api/apay/cards"
        const val CARD_BACKGROUND = "/api/apay/card/logo"
        const val ATTACH_RECEIPT = "api/apay/attach-receipt"
        const val CHECK_PAY_UZCARD = "api/apay/check/{billId}"

        fun instantiate(retrofit: Retrofit): ApayRestService =
            retrofit.create()
    }


}