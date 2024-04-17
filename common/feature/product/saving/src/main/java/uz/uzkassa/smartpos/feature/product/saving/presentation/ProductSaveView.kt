package uz.uzkassa.smartpos.feature.product.saving.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.feature.product.saving.data.model.ProductDetails
import java.math.BigDecimal

internal interface ProductSaveView : MvpView {

    fun onProductForCreate()

    fun onLoadingProductDetails()

    fun onSuccessProductDetails(productDetails: ProductDetails)

    fun onErrorProductDetails(throwable: Throwable)

    fun onCategoryDefined(category: Category)

    fun onBaseUnitDefined(isDefined: Boolean)

    fun onUnitDefined(unit: Unit)

    fun onProductVATChanged(vatRate: BigDecimal?)

    fun onLoadingSave()

    fun onErrorSaveCauseBarcodeNotDefined()

    fun onErrorSaveCauseCategoryNotDefined()

    fun onErrorSaveCauseNameNotDefined()

    fun onErrorSaveCausePriceNotDefined()

    fun onErrorSaveCauseUnitNotDefined()

    fun onErrorSave(throwable: Throwable)

    fun onDismissView()
}