package uz.uzkassa.smartpos.feature.product_marking.dependencies

import uz.uzkassa.smartpos.core.data.manager.scanner.BarcodeScannerManager
import uz.uzkassa.smartpos.core.data.source.resource.marking.dao.ProductMarkingDao
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface ProductMarkingFeatureDependencies {
    val coroutineContextManager: CoroutineContextManager

    val productMarkingDao: ProductMarkingDao

    val productMarkingFeatureCallback: ProductMarkingFeatureCallback

    val productMarkingFeatureArgs: ProductMarkingFeatureArgs

    val barcodeScannerManager: BarcodeScannerManager
}