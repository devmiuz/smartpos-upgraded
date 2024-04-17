package uz.uzkassa.apay.data.exception

import uz.uzkassa.apay.R
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource

class PaymentCanceledException() : Exception(), LocalizableResource {
    override val resourceString: ResourceString =
        StringResource(R.string.fragment_feature_user_cashier_sale_main_scanner_error_product_by_barcode_not_found)
}
