package uz.uzkassa.smartpos.core.data.source.gtpos.exception

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import java.io.Serializable

class GTPOSException(
    val gtposErrorType: GTPOSErrorType,
    override val resourceString: ResourceString = gtposErrorType.resourceString
) : RuntimeException(), LocalizableResource, Serializable