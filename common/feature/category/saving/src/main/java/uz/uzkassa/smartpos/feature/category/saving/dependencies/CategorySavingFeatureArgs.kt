package uz.uzkassa.smartpos.feature.category.saving.dependencies

interface CategorySavingFeatureArgs {

    val branchId: Long

    val categoryParentId: Long?
}