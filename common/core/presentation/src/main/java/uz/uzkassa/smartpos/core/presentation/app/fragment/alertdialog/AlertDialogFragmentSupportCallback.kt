package uz.uzkassa.smartpos.core.presentation.app.fragment.alertdialog

interface AlertDialogFragmentSupportCallback {

    fun onAlertDialogShowing() {
    }

    fun onAlertDialogButtonClicked(which: Int) {
    }
}