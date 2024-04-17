package uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.generator

import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.core.IdGenerator
import java.util.*

class UuidGenerator : IdGenerator {

    override fun generateId() = UUID.randomUUID().toString()
}