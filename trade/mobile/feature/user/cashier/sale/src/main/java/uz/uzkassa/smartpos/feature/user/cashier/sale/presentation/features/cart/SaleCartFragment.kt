package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.manager.scanner.params.BarcodeScannerParams
import uz.uzkassa.smartpos.core.presentation.app.dispatchers.keyevent.OnKeyEventDispatcher
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.utils.app.onKeyEventDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.di.SaleCartComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.cart.features.freeprice.FreePriceDialogFragment
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentFeatureUserCashierSaleMainShoppingBagBinding as ViewBinding

internal class SaleCartFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_sale_main_shopping_bag),
    IHasComponent<SaleCartComponent>, SaleCartView {

    @Inject
    lateinit var lazyPresenter: Lazy<SaleCartPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val onKeyEventCallback: OnKeyEventCallback = OnKeyEventCallback()

    private val binding: ViewBinding by viewBinding()
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(
            target = this,
            onChangeShoppingBagItemTypeClicked = { presenter.changeItemType(it) },
            onDeleteShoppingBagItemTypeClicked = { presenter.deleteItemType(it) }
        )
    }

    override fun getComponent(): SaleCartComponent =
        SaleCartComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onKeyEventDispatcher.addCallback(this, onKeyEventCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
            floatingActionButton.setOnClickListener { presenter.openProductSelectionScreen() }
            frameLayout.setOnClickListener { presenter.openPaymentDetailsScreen() }
        }
    }

    override fun onResume() {
        super.onResume()
        onKeyEventCallback.isEnabled = true
        presenter.setToResume(true)
    }

    override fun onPause() {
        super.onPause()
        onKeyEventCallback.isEnabled = false
        presenter.setToResume(false)
        super.onPause()
    }

    override fun onCartDefined(saleCart: SaleCart) {
        recyclerViewDelegate.set(saleCart.itemTypes)
        binding.apply {
            frameLayout.isEnabled = saleCart.isSaleAllowed
            amountTextView.setTextAmount(
                bigDecimal = saleCart.amount,
                currencyStringResId = R.string.core_presentation_common_amount_currency_uzs
            )
        }
    }

    override fun onErrorProductBarcodeScan(throwable: Throwable) =
        ErrorDialogFragment.show(this, throwable)

    override fun onShowFreePriceInput() =
        FreePriceDialogFragment.show(this)

    override fun onCleared() =
        recyclerViewDelegate.clear()


    override fun onShowChangeNotAllowedAlert(isDelete: Boolean) {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_user_cashier_sale_cart_product_change_alert_title)
                if (isDelete) setMessage(R.string.fragment_feature_user_cashier_sale_cart_product_change_alert_delete_message)
                else setMessage(R.string.fragment_feature_user_cashier_sale_cart_product_change_alert_update_message)
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ ->
                    presenter.dismissChangeNotAllowedAlert()
                }
                setOnDismissListener { presenter.dismissChangeNotAllowedAlert() }
            }
            show()
        }
    }

    override fun onDismissChangeNotAllowedAlert() =
        alertDialogDelegate.dismiss()

    override fun onShowFinishShiftAlert() {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_user_cashier_sale_cart_finish_shift_alert_title)
                setMessage(R.string.fragment_feature_user_cashier_sale_cart_finish_shift_alert_message)
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ ->
                    presenter.dismissFinishShiftAlert()
                }
                setOnDismissListener { presenter.dismissFinishShiftAlert() }
            }
            show()
        }
    }

    override fun onDismissFinishShiftAlert() =
        alertDialogDelegate.dismiss()

    private inner class OnKeyEventCallback :
        OnKeyEventDispatcher.OnKeyEventCallback(isEnabled = false) {

        override fun onKeyEvent(event: KeyEvent?) {
            when (event?.keyCode) {
                KeyEvent.KEYCODE_BACK ->
                    if (event.action == KeyEvent.ACTION_DOWN) presenter.toggleDrawer()
                else -> presenter.setBarcodeScannerParams(
                    BarcodeScannerParams.fromKeyEvent(
                        event = event,
                        isMarking = false
                    )
                )
            }
        }
    }

    companion object {

        fun newInstance() =
            SaleCartFragment().withArguments()
    }
}