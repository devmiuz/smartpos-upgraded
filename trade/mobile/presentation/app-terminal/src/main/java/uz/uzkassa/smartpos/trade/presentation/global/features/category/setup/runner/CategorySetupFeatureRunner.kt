package uz.uzkassa.smartpos.trade.presentation.global.features.category.setup.runner

import ru.terrakok.cicerone.Screen

interface CategorySetupFeatureRunner {

    fun run(branchId: Long, action: (Screen) -> Unit)

    fun back(action: () -> Unit): CategorySetupFeatureRunner

    fun finish(action: () -> Unit): CategorySetupFeatureRunner
}