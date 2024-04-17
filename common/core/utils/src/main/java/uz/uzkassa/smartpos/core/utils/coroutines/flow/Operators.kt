package uz.uzkassa.smartpos.core.utils.coroutines.flow

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

@ObsoleteCoroutinesApi
fun flowInterval(period: Long, delay: Long, unit: TimeUnit): Flow<Long> {
    val tickerChannel: ReceiveChannel<Unit> =
        ticker(delayMillis = unit.toMillis(delay), initialDelayMillis = 0)
    var currentPeriod: Long = 0
    return flow {
        for (event: Unit in tickerChannel) {
            if (currentPeriod == period) tickerChannel.cancel()
            else {
                currentPeriod += 1; emit(currentPeriod)
            }
        }
    }
}