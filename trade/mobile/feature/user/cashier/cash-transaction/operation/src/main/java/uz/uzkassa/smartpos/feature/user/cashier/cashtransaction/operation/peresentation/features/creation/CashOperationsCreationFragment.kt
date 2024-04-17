package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.creation

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.tiper.MaterialSpinner
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.text.RegexInputFilter
import uz.uzkassa.smartpos.core.presentation.utils.widget.addInputFilter
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.core.utils.math.toStringCompat
import uz.uzkassa.smartpos.core.utils.resource.string.get
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.R
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.amount.CashAmount
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.databinding.FragmentFeatureUserCashierCashOperationsCreationBinding  as ViewBinding
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.creation.adapter.CashOperationsSpinnerAdapter
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.creation.di.CashOperationsCreationComponent
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.creation.features.process.CashOperationsProcessFragment
import java.math.BigDecimal
import javax.inject.Inject

internal class CashOperationsCreationFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_cash_operations_creation),
    IHasComponent<CashOperationsCreationComponent>, CashOperationsCreationView,
    MaterialSpinner.OnItemSelectedListener {

    @Inject
    lateinit var lazyPresenter: Lazy<CashOperationsCreationPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val cashOperationsSpinnerAdapter by lazy {
        CashOperationsSpinnerAdapter(requireContext())
    }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): CashOperationsCreationComponent =
        CashOperationsCreationComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { presenter.backToPreviousScreen() }
            }

            cashOperationsTypeSpinner.apply {
                onItemSelectedListener = this@CashOperationsCreationFragment
                adapter = cashOperationsSpinnerAdapter
            }

            cashOperationSaveButton.setOnClickListener {
                presenter.proceedSavingOperation()
            }
            cashAmountTextInputEditText.apply {
                addTextChangedListener {
                    addInputFilter(RegexInputFilter("^[^\\.].*"))
                    cashAmountTextInputLayout.apply { if (error != null) error = null }
                    presenter.setTotalCash(it.toString())
                }
            }

            cashOperationCommentTextInputEditText.setTextChangedListener(this@CashOperationsCreationFragment) { editable ->
                presenter.setMessage(editable?.toString() ?: "")
            }
        }
    }

    override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
        binding.cashAmountTextInputLayout.requestFocus()
        presenter.setOperation(cashOperationsSpinnerAdapter.getItem(position))
    }

    override fun onNothingSelected(parent: MaterialSpinner) {
    }

    override fun onSuccessCashOperations(encashmentOperations: List<CashOperation>) {
        cashOperationsSpinnerAdapter.apply { clear(); addAll(encashmentOperations) }
    }

    override fun onCashOperationChanged(cashOperation: CashOperation) {
        binding.cashOperationsTypeSpinner.apply {
            error = null
            editText?.setText(cashOperation.resourceString.get(requireContext()))
        }
    }

    override fun onLoadingCashAmount() =
        loadingDialogDelegate.show()

    override fun onSuccessCashAmount(cashAmount: CashAmount, cashOperation: CashOperation) {
        loadingDialogDelegate.dismiss()
        when (cashOperation) {
            CashOperation.INCOME ->
                binding.cashAmountTextInputLayout.helperText =
                    getString(R.string.fragment_feature_user_cashier_cash_operations_having_cash)
                        .format(cashAmount.amount.toStringCompat())
            else ->
                binding.cashAmountTextInputLayout.helperText =
                    getString(R.string.fragment_feature_user_cashier_cash_operations_allowed_amount)
                        .format(cashAmount.amount.toStringCompat())
        }
    }

    override fun onFailureCashAmount(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, throwable)
    }

    override fun onCashAmountChanged(amount: BigDecimal) {
        binding.apply {
            cashAmountTextInputLayout.error = null
            cashAmountTextInputEditText.setText(amount.toStringCompat())
        }
    }

    override fun onLoadingCashOperationValidation() =
        loadingDialogDelegate.show()

    override fun onSuccessCashOperationValidation() {
        loadingDialogDelegate.dismiss()
        presenter.showCashOperationProcessView()
    }

    override fun onFailureCashOperationSavingOperationNotDefined() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.cashOperationsTypeSpinner.error =
                getText(R.string.fragment_feature_user_cashier_cash_operations_error_operation_not_defined)
    }

    override fun onShowCashOperationProcessView() =
        CashOperationsProcessFragment.show(this)

    override fun onFailureCashOperationSavingAmountNotValid() {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            binding.cashAmountTextInputLayout.error =
                getText(R.string.fragment_feature_user_cashier_cash_operations_error_amount_not_valid)
    }

    override fun onFailureCashOperationValidation(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            CashOperationsCreationFragment()
                .withArguments()
    }
}