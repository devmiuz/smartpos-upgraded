package uz.uzkassa.smartpos.core.data.source.socket.stomp.session.body

import org.hildan.krossbow.stomp.frame.FrameBody

sealed class StompBody {
    data class Binary(val bytes: ByteArray) : StompBody() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Binary

            if (!bytes.contentEquals(other.bytes)) return false

            return true
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }

    }

    data class Text(val text: String) : StompBody()
    object Empty : StompBody()

    internal companion object {

        fun map(body: FrameBody?) = when (val frameBody: FrameBody? = body) {
            is FrameBody.Binary -> Binary(frameBody.bytes)
            is FrameBody.Text -> Text(frameBody.text)
            else -> Empty
        }
    }
}