package uz.uzkassa.smartpos.trade.presentation.global.runner.action

open class FeatureActionArgs<Args : Any, Action>(
    private val args: Args,
    private val action: Action
) : FeatureAction<Action>(action) {

    open fun act(args: Args, action: (Action) -> Unit) {
        action(this.action)
    }

    @Deprecated(message = "You must use function with args", level = DeprecationLevel.HIDDEN)
    override fun act(action: (Action) -> Unit) =
        super.act(action)
}