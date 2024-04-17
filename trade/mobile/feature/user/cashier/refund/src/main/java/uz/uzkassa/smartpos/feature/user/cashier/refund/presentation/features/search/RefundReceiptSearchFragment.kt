package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import by.kirich1409.viewbindingdelegate.viewBinding
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.feature.user.cashier.refund.R
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.exception.FiscalReceiptNotAllowedException
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.exception.FiscalReceiptNotFoundException
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search.di.RefundReceiptSearchComponent
import javax.inject.Inject
import javax.inject.Provider
import uz.uzkassa.smartpos.feature.user.cashier.refund.databinding.FragmentFeatureUserCashierRefundReceiptSearchBinding as ViewBinding

internal class RefundReceiptSearchFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_refund_receipt_search),
    IHasComponent<RefundReceiptSearchComponent>, RefundReceiptSearchView,
    Toolbar.OnMenuItemClickListener {

    @Inject
    lateinit var presenterProvider: Provider<RefundReceiptSearchPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): RefundReceiptSearchComponent =
        RefundReceiptSearchComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.onCreate(toolbar, savedInstanceState)

            receiptUidTextInputEditText.setTextChangedListener(this@RefundReceiptSearchFragment) {
                receiptUidTextInputLayout.apply { if (error != null) error = null }
                presenter.setUid(it.toString())
            }
            proceedButton.setOnClickListener { presenter.getReceiptByUid() }
        }

        toolbarDelegate.apply {
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            inflateMenu(
                menuResId = R.menu.menu_feature_user_cashier_refund_receipt_search,
                listener = this@RefundReceiptSearchFragment
            )
        }
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.barcode_scanner_menu_item -> presenter.openReceiptQrCameraScannerScreen().let { true }
        else -> false
    }

    override fun onLoadingSearch() =
        loadingDialogDelegate.show()

    override fun onSuccessSearch() {
        loadingDialogDelegate.dismiss()
    }

    override fun onErrorSearchCauseUidNotDefined() {
        loadingDialogDelegate.dismiss()
    }

    override fun onErrorSearchCauseNotFound(throwable: FiscalReceiptNotFoundException) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onErrorSearchCauseNotAllowed(throwable: FiscalReceiptNotAllowedException) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onErrorSearch(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            RefundReceiptSearchFragment().withArguments()
    }
}