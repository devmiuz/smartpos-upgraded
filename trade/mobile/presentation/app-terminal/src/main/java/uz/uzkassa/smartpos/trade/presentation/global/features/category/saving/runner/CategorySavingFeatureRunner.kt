package uz.uzkassa.smartpos.trade.presentation.global.features.category.saving.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category

interface CategorySavingFeatureRunner {

    fun run(branchId: Long, categoryParentId: Long? = null, action: (Screen) -> Unit)

    fun finish(action: (Category) -> Unit): CategorySavingFeatureRunner
}