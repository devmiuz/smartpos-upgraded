package uz.uzkassa.smartpos.trade.presentation.support.dagger2.mediator

import dagger.MapKey
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.reflect.KClass

@Suppress("unused")
@Target(AnnotationTarget.FUNCTION)
@Retention
@MapKey
annotation class FeatureMediatorKey(val value: KClass<out FeatureMediator>)