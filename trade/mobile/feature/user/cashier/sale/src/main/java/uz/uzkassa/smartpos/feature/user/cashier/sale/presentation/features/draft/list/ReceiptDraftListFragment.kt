package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraft
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.setMessage
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.cashier.sale.R
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.creation.ReceiptDraftCreationFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.list.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.draft.list.di.ReceiptDraftListComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.cashier.sale.databinding.FragmentFeatureUserCashierSaleMainReceiptDraftListBinding as ViewBinding

internal class ReceiptDraftListFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_sale_main_receipt_draft_list),
    IHasComponent<ReceiptDraftListComponent>,
    ReceiptDraftListView,
    Toolbar.OnMenuItemClickListener {

    var searchMode = false;

    @Inject
    lateinit var lazyPresenterDraft: Lazy<ReceiptDraftListPresenter>
    private val presenter by moxyPresenter { lazyPresenterDraft.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val alertDialogDelegate by lazy { AlertDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(
            target = this,
            onClicked = { presenter.selectReceiptDraftForRestore(it) },
            onDeleteClicked = { presenter.selectReceiptDraftForDelete(it) }
        )
    }

    override fun getComponent(): ReceiptDraftListComponent =
        ReceiptDraftListComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }
            editTextSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    presenter.getReceiptDrafts(p0.toString())
                }
            })
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.refresh_receipt_drafts_menu_item ->
                presenter.getReceiptDrafts().let { true }
            R.id.search_receipt_drafts_menu_item -> {
                if (searchMode) {
                    item.setIcon(R.drawable.ic_baseline_search_24)
                    binding.apply {
                        toolbarTitle.visibility = View.VISIBLE
                        toolbarSearch.visibility = View.GONE
                    }
                } else {
                    item.setIcon(R.drawable.ic_baseline_close_24)
                    binding.apply {
                        toolbarTitle.visibility = View.GONE
                        toolbarSearch.visibility = View.VISIBLE
                    }
                }
                searchMode = !searchMode
                return true
            }
            else -> false
        }
    }



    override fun onLoadingReceiptDrafts() {

        toolbarDelegate.clearMenu()
        recyclerViewDelegate.onLoading()
    }

    override fun onSuccessReceiptDrafts(receiptDrafts: List<ReceiptDraft>) {
        toolbarDelegate.inflateMenu(
            menuResId = R.menu.menu_feature_user_cashier_sale_receipt_draft_list,
            listener = this@ReceiptDraftListFragment
        )
        recyclerViewDelegate.onSuccess(receiptDrafts)
    }

    override fun onErrorReceiptDrafts(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable) { presenter.getReceiptDrafts() }

    override fun onShowRestoreAlert(receiptId: Long, receiptName: String) {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.core_presentation_common_warning)
                setMessage(R.string.fragment_feature_user_cashier_sale_main_receipt_draft_list_restore_with_creation_message)
                setPositiveButton(R.string.core_presentation_common_answer_yes) { _, _ -> presenter.showReceiptDraftCreation() }
                setNegativeButton(R.string.core_presentation_common_answer_no) { _, _ ->
                    presenter.restoreReceiptDraft(receiptId)
                }
                setOnDismissListener { presenter.dismissAlert() }
            }
        }.show()
    }

    override fun onShowReceiptDraftCreationView() =
        ReceiptDraftCreationFragment.show(this)

    override fun onLoadingRestore() =
        loadingDialogDelegate.show()

    override fun onSuccessRestore() =
        loadingDialogDelegate.dismiss()

    override fun onErrorRestore(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onShowDeleteAlert(receiptDraft: ReceiptDraft) {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.fragment_feature_user_cashier_sale_main_receipt_draft_list_delete_title)
                setMessage(
                    R.string.fragment_feature_user_cashier_sale_main_receipt_draft_list_delete_message,
                    receiptDraft.name
                )
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ ->
                    presenter.deleteReceiptDraft(receiptDraft)
                }
                setNegativeButton(R.string.core_presentation_common_cancel) { _, _ ->
                    presenter.dismissAlert()
                }
                setOnDismissListener { presenter.dismissAlert() }
            }
        }.show()
    }

    override fun onLoadingDelete() =
        loadingDialogDelegate.show()

    override fun onSuccessDelete(receiptDraft: ReceiptDraft) {
        loadingDialogDelegate.dismiss()
        recyclerViewDelegate.remove(receiptDraft)
    }

    override fun onErrorDelete(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onDismissAlert() =
        alertDialogDelegate.dismiss()

    companion object {

        fun newInstance() =
            ReceiptDraftListFragment().withArguments()
    }
}