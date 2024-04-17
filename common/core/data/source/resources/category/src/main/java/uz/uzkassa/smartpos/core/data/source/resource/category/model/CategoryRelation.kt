package uz.uzkassa.smartpos.core.data.source.resource.category.model

data class CategoryRelation(
    val entity: CategoryEntity,
    val relations: List<CategoryRelation>
)