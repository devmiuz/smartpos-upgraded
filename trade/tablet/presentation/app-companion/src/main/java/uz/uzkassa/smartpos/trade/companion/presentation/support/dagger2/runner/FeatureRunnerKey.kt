package uz.uzkassa.smartpos.trade.companion.presentation.support.dagger2.runner

import dagger.MapKey
import uz.uzkassa.smartpos.trade.companion.presentation.support.feature.FeatureRunner
import kotlin.reflect.KClass

@Suppress("unused")
@Target(AnnotationTarget.FUNCTION)
@Retention
@MapKey
annotation class FeatureRunnerKey(val value: KClass<out FeatureRunner>)