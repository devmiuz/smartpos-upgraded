package uz.uzkassa.smartpos.trade.companion.presentation.support.dagger2.runner

import uz.uzkassa.smartpos.trade.companion.presentation.support.feature.FeatureRunner
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class FeatureRunnersManager @Inject constructor(
    private val featureRunners: MutableMap<Class<out FeatureRunner>, Provider<FeatureRunner>>
) {

    @Suppress("UNCHECKED_CAST")
    fun <T : FeatureRunner> bind(kClass: KClass<out T>): T =
        featureRunners[kClass.java]?.get() as T

    inline fun <reified T : FeatureRunner> bind(): T =
        bind(T::class)
}