package uz.uzkassa.smartpos.feature.sync.common.model.result

interface ProgressState {

    val isFinished: Boolean
        get() = this is Success

    interface Start : ProgressState

    interface Success : ProgressState

    interface Failure : ProgressState {
        val throwable: Throwable
    }
}