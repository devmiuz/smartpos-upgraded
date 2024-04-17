package uz.uzkassa.smartpos.trade.presentation.global.features.category.selection.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category

interface CategorySelectionFeatureRunner {

    fun run(branchId: Long, action: (Screen) -> Unit)

    fun finish(action: (Category) -> Unit): CategorySelectionFeatureRunner

}