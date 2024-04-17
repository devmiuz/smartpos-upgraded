package uz.uzkassa.smartpos.feature.check.data.model.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.user.cashier.refund.R

internal class FiscalReceiptNotFoundException : Exception(), LocalizableResource {

    override val resourceString: ResourceString =
        StringResource(R.string.fragment_feature_user_cashier_refund_receipt_search_receipt_not_found_alert_message)
}