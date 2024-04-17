package uz.uzkassa.smartpos.trade.companion.presentation.support.dagger2.mediator

import uz.uzkassa.smartpos.trade.companion.presentation.support.feature.FeatureMediator
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class FeatureMediatorsManager @Inject constructor(
    private val featureMediators: MutableMap<Class<out FeatureMediator>, Provider<FeatureMediator>>
) {

    @Suppress("UNCHECKED_CAST")
    fun <T : FeatureMediator> bind(kClass: KClass<out T>): T =
        featureMediators[kClass.java]?.get() as T

    inline fun <reified T : FeatureMediator> bind(): T =
        bind(T::class)
}