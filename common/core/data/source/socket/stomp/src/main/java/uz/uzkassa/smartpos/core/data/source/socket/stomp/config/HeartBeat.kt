package uz.uzkassa.smartpos.core.data.source.socket.stomp.config

import org.hildan.krossbow.stomp.config.HeartBeat as KHeartBeat

/**
 * Defines heart beats for STOMP sessions, as
 * [defined in the STOMP specification](https://stomp.github.io/stomp-specification-1.2.html#Heart-beating).
 */
data class HeartBeat(
    /**
     * Represents what the sender of the frame can do (outgoing heart-beats).
     * The value 0 means it cannot send heart-beats, otherwise it is the smallest number of milliseconds between
     * heart-beats that it can guarantee.
     */
    val minSendPeriodMillis: Int = 0,
    /**
     * Represents what the sender of the frame would like to get (incoming heart-beats).
     * The value 0 means it does not want to receive heart-beats, otherwise it is the desired number of milliseconds
     * between heart-beats.
     */
    val expectedPeriodMillis: Int = 0
) {

    internal companion object {

        fun map(heartBeat: HeartBeat): KHeartBeat = with(heartBeat) {
            KHeartBeat(minSendPeriodMillis, expectedPeriodMillis)
        }
    }
}