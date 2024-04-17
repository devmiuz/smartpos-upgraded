package uz.uzkassa.smartpos.feature.user.cashier.sale.data.rest

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApayRestService {


    @POST(CREATE_BILL)
    fun createBill(@Body jsonElement: JsonElement): Flow<Unit>

    @PUT(ATTACH_RECEIPT)
    fun attachReceipt(@Body jsonElement: JsonElement): Flow<Unit>

    companion object {
        const val CREATE_BILL = "api/apay/bill"
        const val ATTACH_RECEIPT = "api/apay/attach-receipt"

        fun instantiate(retrofit: Retrofit): ApayRestService =
            retrofit.create()
    }


}