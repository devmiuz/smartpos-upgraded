package uz.uzkassa.smartpos.feature.product_marking.data.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.feature.product_marking.R

internal class NotFoundProductMarkingException : RuntimeException(), LocalizableResource {
    override val resourceString: ResourceString = StringResource(R.string.feature_product_marking_not_found_marking_exception)
}