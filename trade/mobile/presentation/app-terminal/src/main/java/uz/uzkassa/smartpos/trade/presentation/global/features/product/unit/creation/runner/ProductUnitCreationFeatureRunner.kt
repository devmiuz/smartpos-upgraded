package uz.uzkassa.smartpos.trade.presentation.global.features.product.unit.creation.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit

interface ProductUnitCreationFeatureRunner {

    fun run(units: List<ProductUnit>, action: (Screen) -> kotlin.Unit)

    fun finish(action: (List<ProductUnit>) -> kotlin.Unit): ProductUnitCreationFeatureRunner
}