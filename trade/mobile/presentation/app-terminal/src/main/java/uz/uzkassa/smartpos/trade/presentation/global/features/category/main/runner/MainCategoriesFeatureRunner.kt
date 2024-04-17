package uz.uzkassa.smartpos.trade.presentation.global.features.category.main.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface MainCategoriesFeatureRunner : FeatureRunner {

    fun run(branchId: Long, action: (Screen) -> Unit)

    fun back(action: () -> Unit): MainCategoriesFeatureRunner
}