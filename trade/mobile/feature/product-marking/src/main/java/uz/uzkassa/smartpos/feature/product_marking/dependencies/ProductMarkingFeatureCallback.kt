package uz.uzkassa.smartpos.feature.product_marking.dependencies

import uz.uzkassa.smartpos.feature.product_marking.data.model.ProductMarkingResult

interface ProductMarkingFeatureCallback {

    fun onOpenCameraScanner(scannedMarkings: Array<String>)

    fun onFinish(productMarkingResult: ProductMarkingResult)

    fun onBack()
}