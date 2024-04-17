package uz.uzkassa.smartpos.feature.product.unit.creation.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitDetails

internal interface ProductUnitCreationView : MvpView {

    fun onLoadingProductUnitDetails()

    fun onSuccessProductUnitDetails(productUnitDetails: List<ProductUnitDetails>)

    fun onTopClicked(details: ProductUnitDetails)

    fun onBottomClicked(details: ProductUnitDetails)

    fun onSuccessProductUnitDeleted(productUnitDetails: List<ProductUnitDetails>)

    fun onErrorProductUnitDetails(throwable: Throwable)

    fun onDismissView()
}