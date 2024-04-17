package uz.uzkassa.smartpos.core.data.source.resource.socket

import com.tinder.scarlet.Lifecycle

interface ScarletServiceRegistry<T> {

    fun create(lifecycle: Lifecycle): T
}