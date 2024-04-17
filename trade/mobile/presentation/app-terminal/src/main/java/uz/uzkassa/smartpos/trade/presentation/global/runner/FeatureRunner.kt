package uz.uzkassa.smartpos.trade.presentation.global.runner

import uz.uzkassa.smartpos.trade.presentation.global.runner.action.FeatureAction
import kotlin.reflect.KClass

abstract class FeatureRunner {
    private val actionSet: MutableMap<KClass<out FeatureAction<*>>, FeatureAction<*>> =
        HashMap()

    @Suppress("UNCHECKED_CAST")
    fun <Action, T : FeatureAction<Action>> getFeatureAction(kClass: KClass<T>): T {
        return actionSet[kClass] as? T
            ?: throw IllegalStateException("Unable to find action for perform: ${kClass.simpleName}")
    }

    fun setFeatureAction(action: FeatureAction<*>) {
        actionSet[action::class] = action
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <Action, reified T : FeatureAction<Action>> performAction(
        noinline action: (Action) -> Unit
    ) = getFeatureAction(T::class).act(action)
}

object BackAction: FeatureAction<Unit>(Unit)