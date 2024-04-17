package uz.uzkassa.smartpos.trade.presentation.global.features.product.list.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface ProductListFeatureRunner : FeatureRunner {

    fun run(branchId: Long, categoryId: Long, categoryName: String, action: (Screen) -> Unit)

    fun back(action: () -> Unit): ProductListFeatureRunner
}