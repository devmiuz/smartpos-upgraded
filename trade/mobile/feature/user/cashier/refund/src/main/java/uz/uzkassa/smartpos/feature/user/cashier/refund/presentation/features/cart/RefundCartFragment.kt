package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import by.kirich1409.viewbindingdelegate.viewBinding
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextAmount
import uz.uzkassa.smartpos.feature.user.cashier.refund.R
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.mapper.receipt.mapToProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.cart.RefundCart
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.cart.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.cart.di.RefundCartComponent
import javax.inject.Inject
import javax.inject.Provider
import uz.uzkassa.smartpos.feature.user.cashier.refund.databinding.FragmentFeatureUserCashierRefundCartBinding as ViewBinding

internal class RefundCartFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_refund_cart),
    IHasComponent<RefundCartComponent>, RefundCartView, Toolbar.OnMenuItemClickListener {

    @Inject
    lateinit var presenterProvider: Provider<RefundCartPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    private val exitAlertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val requestMarkingAlertDialogDelegate by lazy { AlertDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(
            target = this,
            refundChecked = { t1, t2 -> presenter.setToRefund(t1, t2) },
            refundReceiptProduct = { presenter.openProductQuantity(it) }
        )
    }

    override fun getComponent(): RefundCartComponent =
        RefundCartComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = super.onCreateView(inflater, container, savedInstanceState).apply {
        this?.findViewById<View>(R.id.frame_layout)?.isEnabled = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.onCreate(toolbar, savedInstanceState)
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
            frameLayout.setOnClickListener { presenter.openPaymentScreen() }
        }

        exitAlertDialogDelegate.newBuilder {
            setTitle(R.string.fragment_feature_user_cashier_refund_product_list_exit_alert_title)
            setMessage(R.string.fragment_feature_user_cashier_refund_product_list_exit_alert_message)
            setPositiveButton(R.string.core_presentation_common_cancel) { _, _ ->
                presenter.dismissExitAlert(false)
            }
            setNeutralButton(R.string.core_presentation_common_ok) { _, _ ->
                presenter.dismissExitAlert(true)
            }
            setOnDismissListener { presenter.dismissExitAlert(false) }
        }

        toolbarDelegate.apply {
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            inflateMenu(
                menuResId = R.menu.menu_feature_user_cashier_refund_cart,
                listener = this@RefundCartFragment
            )
        }
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.cart_select_all_menu_item -> presenter.toggleRefundProducts(false).let { true }
        R.id.cart_clear_all_menu_item -> presenter.toggleRefundProducts(true).let { true }
        else -> false
    }

    override fun onRefundReceiptDefined(refundCart: RefundCart) {
        recyclerViewDelegate.set(refundCart.products)
        toolbarDelegate.setSubtitle(
            getString(
                R.string.fragment_feature_user_cashier_refund_product_list_receipt_number,
                refundCart.receiptUid
            )
        )
        binding.apply {
            with(refundCart) {
                availableCashInfo.visibility =
                    if (isCashAvailableForRefund) View.GONE else View.VISIBLE
                frameLayout.isEnabled = isRefundAllowed
                amountTextView.setTextAmount(
                    currencyStringResId = R.string.core_presentation_common_amount_currency_uzs,
                    bigDecimal = amount
                )
            }
        }
    }

    override fun onToggleRefundProducts(isAllProductsSelected: Boolean) {
        prepareOptionsMenu(isAllProductsSelected)
    }

    override fun onShowExitAlert() = exitAlertDialogDelegate.show()

    override fun onDismissExitAlert() = exitAlertDialogDelegate.dismiss()

    override fun onShowRequestMarkingAlert(product: RefundCart.Product) {
        if (!presenter.isInRestoreState(this)) {
            requestMarkingAlertDialogDelegate.newBuilder {
                setTitle(R.string.fragment_feature_user_cashier_refund_product_list_request_marking_alert_title)
                setMessage(R.string.fragment_feature_user_cashier_refund_product_list_request_marking_alert_message)
                setPositiveButton(R.string.core_presentation_common_answer_yes) { _, _ ->
                    presenter.openProductMarking(
                        product.mapToProductQuantity().copy(
                            quantity = product.detailQuantity
                        )
                    )
                }
                setNegativeButton(R.string.core_presentation_common_answer_no) { _, _ ->
                    presenter.openProductQuantity(product)
                }
                setCancelable(false)
                show()
            }
        }
    }

    override fun onDismissRequestMarkingAlert() {
        requestMarkingAlertDialogDelegate.dismiss()
    }

    override fun onErrorAvailableCash(throwable: Throwable) {
        ErrorDialogFragment.show(this, throwable)
    }

    private fun prepareOptionsMenu(isToggleEnabled: Boolean) {
        if (isToggleEnabled) {
            toolbarDelegate.apply {
                findMenuItemById(R.id.cart_select_all_menu_item)?.isVisible = true
                findMenuItemById(R.id.cart_clear_all_menu_item)?.isVisible = false
            }
        } else {
            toolbarDelegate.apply {
                findMenuItemById(R.id.cart_select_all_menu_item)?.isVisible = false
                findMenuItemById(R.id.cart_clear_all_menu_item)?.isVisible = true
            }
        }
    }

    companion object {

        fun newInstance() =
            RefundCartFragment()
                .withArguments()
    }
}