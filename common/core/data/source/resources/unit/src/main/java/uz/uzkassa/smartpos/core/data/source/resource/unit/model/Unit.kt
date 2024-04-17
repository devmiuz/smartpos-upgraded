package uz.uzkassa.smartpos.core.data.source.resource.unit.model

data class Unit(
    val id: Long,
    val code: Long,
    val isCountable: Boolean,
    val name: String,
    val nameRu: String,
    val nameUz: String,
    val description: String
)