@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package uz.uzkassa.smartpos.core.data.source.preference

import android.content.SharedPreferences
import java.lang.Double as JvmDouble

internal class PreferenceSourceImpl(
    private val sharedPreferences: SharedPreferences
) : PreferenceSource {

    override fun getBoolean(tag: String, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(tag, defaultValue)

    override fun getDouble(tag: String, defaultValue: Double): Double =
        JvmDouble.longBitsToDouble(
            sharedPreferences.getLong(tag, JvmDouble.doubleToLongBits(defaultValue))
        )

    override fun getInt(tag: String, defaultValue: Int): Int =
        sharedPreferences.getInt(tag, defaultValue)

    override fun getLong(tag: String, defaultValue: Long): Long =
        sharedPreferences.getLong(tag, defaultValue)

    override fun getString(tag: String, defaultValue: String): String =
        sharedPreferences.getString(tag, defaultValue) ?: ""

    override fun getStringList(tag: String): List<String> =
        sharedPreferences.getStringSet(tag, setOf())?.toList() ?: listOf()

    override fun setBoolean(tag: String, value: Boolean) =
        sharedPreferences.edit().putBoolean(tag, value).apply()

    override fun setDouble(tag: String, value: Double) =
        sharedPreferences.edit().putLong(tag, JvmDouble.doubleToRawLongBits(value)).apply()

    override fun setInt(tag: String, value: Int) =
        sharedPreferences.edit().putInt(tag, value).apply()

    override fun setStringList(tag: String, values: List<String>) =
        sharedPreferences.edit().putStringSet(tag, values.toSet()).apply()

    override fun setLong(tag: String, value: Long) =
        sharedPreferences.edit().putLong(tag, value).apply()

    override fun setString(tag: String, value: String) =
        sharedPreferences.edit().putString(tag, value).apply()

    override fun clear(tag: String) =
        sharedPreferences.edit().remove(tag).apply()

    override fun clearAll() =
        sharedPreferences.edit().clear().apply()

    override fun contains(tag: String): Boolean =
        sharedPreferences.contains(tag)
}