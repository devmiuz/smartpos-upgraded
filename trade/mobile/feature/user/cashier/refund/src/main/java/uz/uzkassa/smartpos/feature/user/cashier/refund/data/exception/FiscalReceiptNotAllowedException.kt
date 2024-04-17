package uz.uzkassa.smartpos.feature.user.cashier.refund.data.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.user.cashier.refund.R

internal class FiscalReceiptNotAllowedException : Exception(), LocalizableResource {

    override val resourceString: ResourceString =
        StringResource(R.string.fragment_feature_user_cashier_refund_receipt_search_receipt_not_allowed_for_refund_alert_message)
}