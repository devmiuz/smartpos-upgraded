package uz.uzkassa.smartpos.trade.data.database.impl.migration

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.OPEN_READONLY
import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import uz.uzkassa.smartpos.trade.data.database.impl.migration.support.SupportDatabaseMigration

class DatabaseMigration(databasePath: String, private val currentVersion: Int) {
    private val version: Int by lazy {
        runCatching { SQLiteDatabase.openDatabase(databasePath, null, OPEN_READONLY) }
            .map { it.version }
            .getOrDefault(currentVersion)
    }

    val migrations: Array<Migration> by lazy {
        if (version == currentVersion) return@lazy emptyArray<Migration>()

        val migrations: List<SupportDatabaseMigration> =
            MigrationsHolder.supportMigrations.filter {
                it.version > version
            }

        val migration: Migration = object : Migration(version, currentVersion) {
            override fun migrate(database: SupportSQLiteDatabase) {
                migrations.forEach { it.proceed(database) }
            }
        }

        return@lazy arrayOf(migration)
    }
}