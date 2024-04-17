package uz.uzkassa.smartpos.feature.helper.product.quantity.data.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.helper.product.quantity.R

internal class ProductQuantityNotDefinedException : RuntimeException(), LocalizableResource {

    override val resourceString: ResourceString =
        StringResource(R.string.fragment_feature_helper_product_quantity_main_change_count_error_not_inputted)
}