package uz.uzkassa.smartpos.trade.data.database.impl.migration

import androidx.sqlite.db.SupportSQLiteDatabase
import uz.uzkassa.smartpos.trade.data.database.impl.migration.support.SupportDatabaseMigration

object MigrationsHolder {
    val supportMigrations: Array<SupportDatabaseMigration> =
        arrayOf(
            Migration2,
            Migration3,
            Migration4,
            Migration6,
            Migration7,
            Migration8,
            Migration9,
            Migration10,
            Migration11,
            Migration12,
            Migration13,
            Migration14,
            Migration15,
            Migration16
        )

    private object Migration2 : SupportDatabaseMigration {
        override val version: Int = 2
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE postpone_receipts ADD COLUMN postpone_receipt_uid TEXT")
            database.execSQL("ALTER TABLE postpone_receipts ADD COLUMN postpone_receipt_date TEXT")
            database.execSQL("ALTER TABLE receipts ADD COLUMN receipt_total_excise TEXT NOT NULL DEFAULT '0'")
            database.execSQL("CREATE TABLE IF NOT EXISTS receipt_payments (receipt_payment_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, receipt_payment_receipt_uid TEXT NOT NULL, receipt_payment_amount TEXT NOT NULL, receipt_payment_type TEXT NOT NULL)")
            database.execSQL("CREATE TABLE IF NOT EXISTS shift_reports (shift_report_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, shift_report_start_date TEXT, shift_report_finish_date TEXT, shift_report_fiscal_shift_number INTEGER NOT NULL, shift_report_total_refund_vat TEXT NOT NULL, shift_report_total_refund_card TEXT NOT NULL, shift_report_total_refund_cash TEXT NOT NULL, shift_report_total_refund_count INTEGER NOT NULL, shift_report_total_sale_vat TEXT NOT NULL, shift_report_total_sale_card TEXT NOT NULL, shift_report_total_sale_cash TEXT NOT NULL, shift_report_total_sale_count INTEGER NOT NULL, shift_report_user_id INTEGER NOT NULL, shift_report_is_synced INTEGER NOT NULL)")
            database.execSQL("ALTER TABLE users ADD COLUMN user_is_dismissed INTEGER NOT NULL DEFAULT '0'")
        }
    }

    private object Migration3 : SupportDatabaseMigration {
        override val version: Int = 3
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS receipt_details_migration (receipt_detail_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, receipt_detail_receipt_uid TEXT NOT NULL, receipt_detail_product_id INTEGER, receipt_detail_product_package_type_id INTEGER, receipt_detail_unit_id INTEGER, receipt_detail_amount TEXT NOT NULL, receipt_detail_discount TEXT NOT NULL, receipt_detail_discount_percent REAL NOT NULL, receipt_detail_excise_amount TEXT, receipt_detail_excise_rate_amount TEXT, receipt_detail_vat_amount TEXT, receipt_detail_vat_percent REAL, receipt_detail_quantity REAL NOT NULL, receipt_detail_price TEXT NOT NULL, receipt_detail_status TEXT NOT NULL, receipt_detail_product_name TEXT NOT NULL)")
            database.execSQL("INSERT INTO receipt_details_migration (receipt_detail_id, receipt_detail_receipt_uid, receipt_detail_product_id, receipt_detail_product_package_type_id, receipt_detail_unit_id, receipt_detail_amount, receipt_detail_discount, receipt_detail_discount_percent, receipt_detail_excise_amount, receipt_detail_excise_rate_amount, receipt_detail_vat_amount, receipt_detail_vat_percent, receipt_detail_quantity, receipt_detail_price, receipt_detail_status, receipt_detail_product_name) SELECT receipt_detail_id, receipt_detail_receipt_uid, receipt_detail_product_id, receipt_detail_product_package_type_id, receipt_detail_unit_id, receipt_detail_amount, receipt_detail_discount, receipt_detail_discount_percent, receipt_detail_excise_amount, receipt_detail_excise_rate_amount, receipt_detail_vat_amount, receipt_detail_vat_percent, receipt_detail_quantity, receipt_detail_price, receipt_detail_status, receipt_detail_product_name FROM receipt_details")
            database.execSQL("DROP TABLE receipt_details")
            database.execSQL("ALTER TABLE receipt_details_migration RENAME TO receipt_details")

        }
    }

    private object Migration4 : SupportDatabaseMigration {
        override val version: Int = 4
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS receipt_drafts (receipt_draft_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, receipt_draft_receipt_uid TEXT NOT NULL, receipt_draft_name TEXT NOT NULL, receipt_draft_is_remote INTEGER NOT NULL)");
        }
    }

    private object Migration6 : SupportDatabaseMigration {
        override val version: Int = 6
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE products ADD COLUMN product_vat_rate TEXT")
        }
    }

    private object Migration7 : SupportDatabaseMigration {
        override val version: Int = 7
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS product_marking (product_marking_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, product_marking_product_id INTEGER, product_marking TEXT NOT NULL)")
            database.execSQL("ALTER TABLE products ADD COLUMN product_has_mark INTEGER NOT NULL DEFAULT '0'")
            database.execSQL("ALTER TABLE receipt_details ADD COLUMN receipt_detail_marks TEXT")
        }
    }

    private object Migration8 : SupportDatabaseMigration {
        override val version: Int = 8
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE receipt_details ADD COLUMN receipt_detail_category_id INTEGER")
            database.execSQL("ALTER TABLE receipt_details ADD COLUMN receipt_detail_category_name TEXT")
            database.execSQL("ALTER TABLE receipt_details ADD COLUMN receipt_detail_unit_name TEXT")
            database.execSQL("ALTER TABLE receipt_details ADD COLUMN receipt_detail_product_barcode TEXT")
        }
    }

    private object Migration9 : SupportDatabaseMigration {
        override val version: Int = 9
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE products ADD COLUMN vat_barcode TEXT")
            database.execSQL("ALTER TABLE receipt_details ADD COLUMN receipt_detail_product_vat_barcode TEXT")
        }
    }

    private object Migration10 : SupportDatabaseMigration {
        override val version: Int = 10
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE receipts ADD COLUMN receipt_customer_name TEXT")
            database.execSQL("ALTER TABLE receipts ADD COLUMN receipt_customer_contact TEXT")
            database.execSQL("ALTER TABLE receipts ADD COLUMN receipt_readonly INTEGER")
            database.execSQL("ALTER TABLE receipts ADD COLUMN receipt_force_to_print INTEGER")
        }
    }

    private object Migration11 : SupportDatabaseMigration {
        override val version: Int = 11
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE products ADD COLUMN committent_tin TEXT DEFAULT null")
            database.execSQL("ALTER TABLE products ADD COLUMN vat_percent REAL DEFAULT null")
            database.execSQL("ALTER TABLE products ADD COLUMN label TEXT DEFAULT null")
            database.execSQL("ALTER TABLE receipt_details ADD COLUMN receipt_detail_product_committent_tin TEXT DEFAULT NULL")
            database.execSQL("ALTER TABLE receipt_details ADD COLUMN receipt_detail_product_label TEXT DEFAULT NULL")
        }
    }


    private object Migration12 : SupportDatabaseMigration {
        override val version: Int = 12
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE companies ADD COLUMN payment_types TEXT DEFAULT NULL")
        }
    }

    private object Migration13 : SupportDatabaseMigration {
        override val version: Int = 13
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE receipts ADD COLUMN receipt_terminal_id TEXT DEFAULT NULL")
            database.execSQL("ALTER TABLE receipts ADD COLUMN receipt_seq TEXT DEFAULT NULL")
            database.execSQL("ALTER TABLE receipts ADD COLUMN fiscal_receipt_created_date TEXT DEFAULT NULL")
        }
    }

    private object Migration14 : SupportDatabaseMigration {
        override val version: Int = 14
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE receipts ADD COLUMN payment_bill_id TEXT DEFAULT NULL")
        }
    }

    private object Migration15 : SupportDatabaseMigration {
        override val version: Int = 15
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE receipts ADD COLUMN receipt_base_status TEXT NOT NULL DEFAULT 'PAID'")
        }
    }

    private object Migration16 : SupportDatabaseMigration {
        override val version: Int = 16
        override fun proceed(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE receipts ADD COLUMN transaction_id TEXT DEFAULT NULL")
            database.execSQL("ALTER TABLE receipts ADD COLUMN payment_provider_id INTEGER DEFAULT NULL")
        }
    }
}