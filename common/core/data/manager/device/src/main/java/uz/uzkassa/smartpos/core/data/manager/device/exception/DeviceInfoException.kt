package uz.uzkassa.smartpos.core.data.manager.device.exception

import uz.uzkassa.engine.device.integration.common.error.EngineError
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import java.io.Serializable

class DeviceInfoException internal constructor(
    val deviceInfoErrorType: DeviceInfoErrorType,
    override val resourceString: ResourceString = deviceInfoErrorType.resourceString
) : RuntimeException(), LocalizableResource, Serializable {


    internal constructor(engineError: EngineError) : this(DeviceInfoErrorType.valueOf(engineError))
}