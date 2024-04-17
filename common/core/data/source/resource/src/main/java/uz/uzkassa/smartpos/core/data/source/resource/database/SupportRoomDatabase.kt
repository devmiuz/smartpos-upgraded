package uz.uzkassa.smartpos.core.data.source.resource.database

import androidx.room.RoomDatabase
import kotlin.reflect.KClass

abstract class SupportRoomDatabase : RoomDatabase() {

    abstract fun <T: BaseDao> getDao(kClass: KClass<*>): T

    inline fun <reified T: BaseDao> getDao(): T {
        return getDao(T::class)
    }

    abstract fun clearAll()
}