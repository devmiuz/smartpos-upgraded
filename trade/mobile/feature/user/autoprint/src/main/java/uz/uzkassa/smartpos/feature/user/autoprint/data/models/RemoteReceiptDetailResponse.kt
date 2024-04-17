package uz.uzkassa.smartpos.feature.user.autoprint.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.uzkassa.smartpos.feature.user.autoprint.data.serializer.RemoteReceiptBigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class RemoteReceiptDetailResponse(
    @SerialName("productId")
    val productId: Long?,
    @SerialName("productName")
    val productName: String?,
    @SerialName("productBarcode")
    val productBarcode: String?,
    @SerialName("unitId")
    val unitId: Long?,

    @Serializable(with = RemoteReceiptBigDecimalSerializer.Nullable::class)
    @SerialName("price")
    val price: BigDecimal? = BigDecimal.ZERO,

    @SerialName("qty")
    val quantity: Double? = 0.0,

    @Serializable(with = RemoteReceiptBigDecimalSerializer.Nullable::class)
    @SerialName("amount")
    val amount: BigDecimal? = BigDecimal.ZERO,

    @Serializable(with = RemoteReceiptBigDecimalSerializer.Nullable::class)
    @SerialName("nds")
    val vatAmount: BigDecimal? = BigDecimal.ZERO,

    @SerialName("ndsPercent")
    val vatPercent: Double? = 0.0
)