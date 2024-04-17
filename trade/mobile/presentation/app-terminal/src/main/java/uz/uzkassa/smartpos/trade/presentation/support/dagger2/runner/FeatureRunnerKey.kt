package uz.uzkassa.smartpos.trade.presentation.support.dagger2.runner

import dagger.MapKey
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner
import kotlin.reflect.KClass

@Suppress("unused")
@Target(AnnotationTarget.FUNCTION)
@Retention
@MapKey
annotation class FeatureRunnerKey(val value: KClass<out FeatureRunner>)