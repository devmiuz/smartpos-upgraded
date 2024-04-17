package uz.uzkassa.smartpos.trade.data.database.impl.migration.support

import androidx.sqlite.db.SupportSQLiteDatabase

interface SupportDatabaseMigration {

    val version: Int

    fun proceed(database: SupportSQLiteDatabase)
}