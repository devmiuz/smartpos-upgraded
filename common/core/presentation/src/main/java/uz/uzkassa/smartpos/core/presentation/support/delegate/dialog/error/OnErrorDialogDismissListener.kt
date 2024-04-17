package uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error

interface OnErrorDialogDismissListener {

    fun onErrorDialogDismissed(throwable: Throwable?)
}