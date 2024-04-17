package uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail

import kotlinx.serialization.*
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model.ProductPackageTypeResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatusResponse
import uz.uzkassa.smartpos.core.utils.kserialization.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class ReceiptDetailResponse(
    @SerialName("categoryId")
    val categoryId: Long?,

    @SerialName("categoryName")
    val categoryName: String?,

    @SerialName("productId")
    val productId: Long?,

    @SerialName("unitId")
    val unitId: Long? = null,

    @SerialName("unitName")
    val unitName: String?,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("amount")
    val amount: BigDecimal,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("discount")
    val discountAmount: BigDecimal? = null,

    @SerialName("discountPercent")
    val discountPercent: Double? = null,

    @Serializable(with = BigDecimalSerializer.Nullable::class)
    @SerialName("excise")
    val exciseAmount: BigDecimal? = null,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("exciseRate")
    val exciseRateAmount: BigDecimal? = null,

    @Serializable
    @SerialName("marks")
    val marks: Array<String>? = null,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("nds")
    val vatAmount: BigDecimal? = null,

    @SerialName("ndsPercent")
    val vatPercent: Double? = null,

    @SerialName("qty")
    val quantity: Double,

    @Serializable(with = BigDecimalSerializer.NotNullable::class)
    @SerialName("price")
    val price: BigDecimal,

    @SerialName("packageType")
    val packageType: ProductPackageTypeResponse? = null,

    @Serializable(with = ReceiptStatusResponseSerializer::class)
    @SerialName("status")
    val status: ReceiptStatusResponse? = ReceiptStatusResponse.PAID,

    @SerialName("productBarcode")
    val barcode: String?,

    @SerialName("vatBarcode")
    val vatBarcode: String?,

    @SerialName("productName")
    @Serializable
    val name: String,

    @SerialName("committentTin")
    val committentTin: String?,

    @SerialName("label")
    val label: String? = null
) {

    private object ReceiptStatusResponseSerializer : KSerializer<ReceiptStatusResponse> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "ReceiptStatusResponseSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun deserialize(decoder: Decoder): ReceiptStatusResponse =
            ReceiptStatusResponse.valueOf(decoder.decodeString())

        override fun serialize(encoder: Encoder, value: ReceiptStatusResponse) =
            encoder.encodeString(value.name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReceiptDetailResponse

        if (productId != other.productId) return false
        if (unitId != other.unitId) return false
        if (amount != other.amount) return false
        if (discountAmount != other.discountAmount) return false
        if (discountPercent != other.discountPercent) return false
        if (exciseAmount != other.exciseAmount) return false
        if (exciseRateAmount != other.exciseRateAmount) return false
        if (marks != null) {
            if (other.marks == null) return false
            if (!marks.contentEquals(other.marks)) return false
        } else if (other.marks != null) return false
        if (vatAmount != other.vatAmount) return false
        if (vatPercent != other.vatPercent) return false
        if (quantity != other.quantity) return false
        if (price != other.price) return false
        if (packageType != other.packageType) return false
        if (status != other.status) return false
        if (name != other.name) return false
        if (committentTin != other.committentTin) return false
        if (label != other.label) return false

        return true
    }

    override fun hashCode(): Int {
        var result = productId?.hashCode() ?: 0
        result = 31 * result + (unitId?.hashCode() ?: 0)
        result = 31 * result + amount.hashCode()
        result = 31 * result + (discountAmount?.hashCode() ?: 0)
        result = 31 * result + (discountPercent?.hashCode() ?: 0)
        result = 31 * result + (exciseAmount?.hashCode() ?: 0)
        result = 31 * result + (exciseRateAmount?.hashCode() ?: 0)
        result = 31 * result + (marks?.contentHashCode() ?: 0)
        result = 31 * result + (vatAmount?.hashCode() ?: 0)
        result = 31 * result + (vatPercent?.hashCode() ?: 0)
        result = 31 * result + quantity.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + (packageType?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + committentTin.hashCode()
        result = 31 * result + label.hashCode()
        return result
    }
}