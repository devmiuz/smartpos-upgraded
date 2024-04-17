package uz.uzkassa.smartpos.core.data.source.resource.database

import androidx.room.*

interface BaseDao {

    abstract class Impl<T : Any> : BaseDao {

        @Delete
        abstract fun delete(collection: Collection<T>)

        @Delete
        abstract fun delete(value: T)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        protected abstract fun insert(value: T): Long

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        protected abstract fun insert(collection: Collection<T>): LongArray

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        abstract fun insertOrUpdate(value: T)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        abstract fun insertOrUpdate(collection: Collection<T>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        abstract fun update(value: T): Long

        @Update(onConflict = OnConflictStrategy.REPLACE)
        abstract fun update(collection: Collection<T>)

        @Transaction
        open fun upsert(value: T): Long =
            insert(value).let {
                if (it == -1L) update(value) else it
            }

        @Transaction
        open fun upsert(collection: Collection<T>) {
            val list: List<T> = collection.toList()
            val insert: LongArray = insert(list)
            val update: MutableList<T> = mutableListOf()
            insert.forEachIndexed { i, l -> if (l == -1L) update.add(list[i]) }
            if (update.isNotEmpty()) update(update)
        }
    }
}