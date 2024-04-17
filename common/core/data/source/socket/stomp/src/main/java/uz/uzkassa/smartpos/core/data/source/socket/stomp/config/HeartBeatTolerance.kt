package uz.uzkassa.smartpos.core.data.source.socket.stomp.config

import  org.hildan.krossbow.stomp.config.HeartBeatTolerance as KHeartBeatTolerance

/**
 * Defines tolerance for heart beats.
 *
 * If both the client and server really stick to the heart beats periods negotiated and given by the CONNECTED frame,
 * network latencies will make them miss their marks. That's why we need some sort of tolerance.
 *
 * In case the server is too strict about its expectations, we can send heart beats a little bit earlier than we're
 * supposed to (see [outgoingMarginMillis]).
 *
 * In case the server really sticks to its own period without such margin, we need to allow a little bit of delay to
 * make up for network latencies before we fail and close the connection (see [incomingMarginMillis]).
 */
data class HeartBeatTolerance(
    /**
     * How many milliseconds in advance heart beats should be sent.
     * This is to avoid issues when servers are not very tolerant on heart beats reception.
     *
     * By default, we expect the server to be tolerant in its expectations, so we send heart beats on time.
     */
    val outgoingMarginMillis: Int = 0,
    /**
     * How much more to wait before failing when we don't receive a heart beat from the server in the expected time.
     *
     * By default, we expect the server to send heart beats on time, so we add some margin in our own expectation.
     */
    val incomingMarginMillis: Int = 500
) {

    internal companion object {

        fun map(tolerance: HeartBeatTolerance): KHeartBeatTolerance = with(tolerance) {
            KHeartBeatTolerance(outgoingMarginMillis, incomingMarginMillis)
        }
    }
}