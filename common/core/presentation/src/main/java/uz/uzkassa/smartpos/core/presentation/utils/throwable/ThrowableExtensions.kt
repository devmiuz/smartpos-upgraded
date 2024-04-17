package uz.uzkassa.smartpos.core.presentation.utils.throwable

import android.content.Context
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.get
import java.net.ConnectException

fun Throwable.getLocalizedMessage(context: Context): CharSequence = when (this) {
    is ConnectException -> context.getString(R.string.data_source_http_error_connection)
    is LocalizableResource -> resourceString.get(context)
    else -> context.getString(R.string.data_source_unknown_error)
}