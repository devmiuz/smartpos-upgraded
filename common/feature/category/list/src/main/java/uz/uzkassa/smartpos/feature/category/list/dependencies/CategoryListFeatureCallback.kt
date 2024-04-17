package uz.uzkassa.smartpos.feature.category.list.dependencies

interface CategoryListFeatureCallback {

    fun onCreateCategory(categoryParentId: Long?)

    fun onCategorySelected(categoryId: Long, categoryName: String)

    fun onBackFromCategoryList()
}