package uz.uzkassa.smartpos.core.data.source.resource.language.model

import androidx.room.ColumnInfo

data class LanguageEntity(
    @ColumnInfo(name = "language_key")
    val languageKey: String
)