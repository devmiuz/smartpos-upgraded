package uz.uzkassa.smartpos.feature.product_marking.presentation.scanner

import moxy.MvpView

internal interface MarkingScannerView: MvpView {

    fun setProductCount(totalQuantity: Int, markedQuantity: Int)

    fun onUndefinedError()

    fun onDuplicateError(throwable: Throwable)

    fun onDismissView()
}