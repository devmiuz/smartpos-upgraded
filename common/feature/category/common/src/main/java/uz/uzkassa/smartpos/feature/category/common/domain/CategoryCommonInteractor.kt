package uz.uzkassa.smartpos.feature.category.common.domain

import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category

abstract class CategoryCommonInteractor {
    private val categories: MutableList<Category> = arrayListOf()

    protected fun hasAtLeastOneEnabledCategory(): Boolean =
        hasAtLeastOneEnabledCategory(categories)

    protected fun getListCategories(): List<Category> =
        categories

    protected fun setCategories(categories: List<Category>) {
        this.categories.apply { clear(); addAll(categories) }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    protected fun upsertCategory(category: Category, isAddingCategory: Boolean): List<Category> {
        val upsertCategory: Category =
            (getUpdatedCategory(category, isAddingCategory = isAddingCategory))

        return categories.apply {
            find { it.id == upsertCategory.id }.let {
                if (it != null) {
                    val index: Int = indexOf(it)
                    add(index, upsertCategory)
                    removeAt(index + 1)
                } else add(upsertCategory)
            }
        }.sortedBy { it.name }
    }

    protected fun filterBy(predicate: (category: Category) -> Boolean): List<Category> =
        filterBy(predicate, this.categories)

    private fun filterBy(
        predicate: (category: Category) -> Boolean, categories: List<Category>
    ): List<Category> {
        val filteredCategories: MutableList<Category> = arrayListOf()
        categories
            .filter(predicate)
            .forEach {
                val childCategories: List<Category> =
                    if (it.childCategories.isNotEmpty()) filterBy(predicate, it.childCategories)
                    else emptyList()
                filteredCategories.add(it.copy(childCategories = childCategories))
            }

        return filteredCategories
    }

    @Suppress("LABEL_NAME_CLASH")
    private fun getUpdatedCategory(
        category: Category,
        parentCategory: Category? = getParentCategory(category),
        isAddingCategory: Boolean = false
    ): Category {
        return when {
            parentCategory == null -> category
            category.id == parentCategory.id -> category
            else -> {
                val childCategories: MutableList<Category> =
                    parentCategory.childCategories.toMutableList()

                val resultCategories: MutableList<Category> = mutableListOf()

                if (category.parentId == parentCategory.id) {
                    if (isAddingCategory) childCategories.add(category)
                    else childCategories.find { it.id == category.id }
                        ?.let { childCategories.remove(it) }

                    return parentCategory.copy(childCategories = childCategories)
                }

                childCategories.forEach {
                    val updatedCategory: Category =
                        getUpdatedCategory(category, it, isAddingCategory)
                    resultCategories.add(updatedCategory)
                }

                return parentCategory.copy(childCategories = resultCategories)
            }
        }
    }

    private fun getParentCategory(
        category: Category,
        childCategories: List<Category> = categories
    ): Category? {
        return if (category.parentId == null) category
        else {
            var childCategory: Category? = null
            childCategories.forEach {
                if (childCategory == null) {
                    childCategory = if (it.id == category.parentId) getParentCategory(it)
                    else getParentCategory(category, it.childCategories)
                }
            }

            childCategory
        }
    }

    private fun hasAtLeastOneEnabledCategory(categories: List<Category>): Boolean {
        var hasEnabledCategories = false
        categories.forEach {
            if (it.isEnabled) hasEnabledCategories = true
            if (!hasEnabledCategories) {
                if (it.childCategories.isNotEmpty())
                    hasEnabledCategories = hasAtLeastOneEnabledCategory(it.childCategories)
            }
        }
        return hasEnabledCategories
    }
}