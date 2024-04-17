package uz.uzkassa.smartpos.feature.product.saving.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.product.saving.data.exception.ProductSavingException
import uz.uzkassa.smartpos.feature.product.saving.dependencies.ProductSavingFeatureCallback
import uz.uzkassa.smartpos.feature.product.saving.domain.ProductSaveInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class ProductSavePresenter @Inject constructor(
    private val categorySelectionLazyFlow: Lazy<Flow<Category>>,
    private val productSavingFeatureCallback: ProductSavingFeatureCallback,
    private val productSaveInteractor: ProductSaveInteractor,
    private val productUnitsLazyFlow: Lazy<Flow<List<ProductUnit>>>,
    private val productVATRateLazyFlow: Lazy<Flow<BigDecimal?>>
) : MvpPresenter<ProductSaveView>() {
    private var isProductCreated: Boolean = false

    override fun onFirstViewAttach() {
        getSelectedCategory()
        getProductDetails()
        getProvidedProductUnits()
        getProvidedProductVATRate()
        viewState.onProductForCreate()
    }

    private fun getSelectedCategory() {
        categorySelectionLazyFlow.get()
            .onEach {
                productSaveInteractor.setCategory(it)
                viewState.onCategoryDefined(it)
            }
            .launchIn(presenterScope)
    }

    private fun getProvidedProductUnits() {
        productUnitsLazyFlow.get()
            .onEach {
                viewState.onBaseUnitDefined(it.size < 2)
                productSaveInteractor.setProductUnits(it)
            }
            .launchIn(presenterScope)
    }

    private fun getProvidedProductVATRate() {
        productVATRateLazyFlow.get()
            .onEach {
                productSaveInteractor.setProductVATRate(it)
                viewState.onProductVATChanged(it)
            }
            .launchIn(presenterScope)
    }

    fun getProductDetails() {
        productSaveInteractor
            .getProductDetails()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingProductDetails() }
            .onSuccess { it ->
                viewState.onSuccessProductDetails(it)
                isProductCreated = it.product != null
                it.product?.let { product ->
                    viewState.apply {
                        product.category?.let { onCategoryDefined(it) }
                        product.unit?.let { onUnitDefined(it) }
                    }
                    viewState.onBaseUnitDefined(it.productUnits.size < 2)
                    viewState.onProductVATChanged(product.vatRate)
                }
            }
            .onFailure { viewState.onErrorProductDetails(it) }
    }

    fun toggleProductVAT(value: Boolean) {
        if (!value) openProductVATRateSelectionView()
        else {
            productSaveInteractor.setProductVATRate(null)
            viewState.onProductVATChanged(null)
        }
    }

    fun toggleProductMark(value: Boolean) =
        productSaveInteractor.setHasMark(value)

    fun setBarcode(value: String) =
        productSaveInteractor.setBarcode(value)

    fun setVatBarcode(value: String) =
        productSaveInteractor.setVatBarcode(value)

    fun setModel(value: String) =
        productSaveInteractor.setModel(value)

    fun setMeasurement(value: String) =
        productSaveInteractor.setMeasurement(value)

    fun setCommittentTin(value: String) = productSaveInteractor.setCommittentTin(value)

    fun setProductUnits() {
        productSaveInteractor.getProductUnits().let {
            productSavingFeatureCallback.onOpenProductUnitCreation(it)
        }
    }

    fun setSize(value: String) =
        productSaveInteractor.setSize(value)

    fun setSalesPrice(value: String) =
        productSaveInteractor.setSalesPrice(value)

    fun setName(value: String) =
        productSaveInteractor.setName(value)

    fun setUnit(value: Unit) {
        productSaveInteractor.setUnit(value)
        viewState.onUnitDefined(value)
    }

    fun dismiss() =
        viewState.onDismissView()

    fun saveProduct() {
        productSaveInteractor
            .saveProduct()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingSave() }
            .onSuccess {
                productSavingFeatureCallback.onFinishProductSaving(it)
                viewState.onDismissView()
            }
            .onFailure {
                when (it) {
                    is ProductSavingException -> {
                        if (it.isBarcodeNotDefined)
                            viewState.onErrorSaveCauseBarcodeNotDefined()
                        if (it.isCategoryNotDefined)
                            viewState.onErrorSaveCauseCategoryNotDefined()
                        if (it.isNameNotDefined)
                            viewState.onErrorSaveCauseNameNotDefined()
                        if (it.isPriceNotDefined)
                            viewState.onErrorSaveCausePriceNotDefined()
                        if (it.isUnitNotDefined)
                            viewState.onErrorSaveCauseUnitNotDefined()
                    }
                    else -> viewState.onErrorSave(it)
                }
            }
    }

    fun openCategorySelectionScreen() {
        productSavingFeatureCallback.onOpenCategorySelection()
    }

    fun openProductVATRateSelectionView() {
        productSaveInteractor.getProductVATRate().let {
            productSavingFeatureCallback.onOpenProductVATRateSelection(it)
        }
    }
}