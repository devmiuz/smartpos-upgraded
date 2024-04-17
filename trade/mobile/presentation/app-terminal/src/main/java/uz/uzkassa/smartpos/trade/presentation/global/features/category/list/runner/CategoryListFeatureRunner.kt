package uz.uzkassa.smartpos.trade.presentation.global.features.category.list.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface CategoryListFeatureRunner : FeatureRunner {

    fun run(branchId: Long, categoryParentId: Long?=null, action: (Screen) -> Unit)

    fun back(action: () -> Unit): CategoryListFeatureRunner
}