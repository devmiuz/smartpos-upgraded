package uz.uzkassa.smartpos.core.utils.kserialization.serializer

import kotlinx.serialization.*
import java.math.BigDecimal

object BigDecimalSerializer {

    object NotNullable : KSerializer<BigDecimal> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "NotNullableBigDecimalSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun deserialize(decoder: Decoder): BigDecimal =
            BigDecimal(decoder.decodeString())

        override fun serialize(encoder: Encoder, value: BigDecimal) {
            encoder.encodeString(value.toString())
        }
    }

    object Nullable : KSerializer<BigDecimal?> {
        override val descriptor = PrimitiveDescriptor(
            serialName = "NullableBigDecimalSerializer",
            kind = PrimitiveKind.STRING
        )

        override fun deserialize(decoder: Decoder): BigDecimal? =
            BigDecimal(decoder.decodeString())

        override fun serialize(encoder: Encoder, value: BigDecimal?) {
            value?.let { encoder.encodeString(it.toString()) } ?: encoder.encodeNull()
        }
    }
}