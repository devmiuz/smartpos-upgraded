package uz.uzkassa.smartpos.trade.presentation.global.runner.action

open class FeatureAction<Action>(private val action: Action) {

    open fun act(action: (Action) -> Unit) {
        action(this.action)
    }
}