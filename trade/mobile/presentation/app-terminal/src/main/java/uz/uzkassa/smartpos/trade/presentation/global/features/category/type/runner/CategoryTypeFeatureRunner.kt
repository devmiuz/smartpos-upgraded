package uz.uzkassa.smartpos.trade.presentation.global.features.category.type.runner

import ru.terrakok.cicerone.Screen

interface CategoryTypeFeatureRunner {

    fun run(branchId: Long, action: (Screen) -> Unit)

    fun back(action: () -> Unit): CategoryTypeFeatureRunner
}