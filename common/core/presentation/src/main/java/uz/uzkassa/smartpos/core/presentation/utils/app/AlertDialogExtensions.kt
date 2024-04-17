package uz.uzkassa.smartpos.core.presentation.utils.app

import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog

fun AlertDialog.Builder.setMessage(
    @StringRes messageId: Int,
    vararg args: Any
): AlertDialog.Builder =
    setMessage(context.getString(messageId, *args))