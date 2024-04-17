package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.utils.math.toStringCompat
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.R
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.details.CashOperationsDetails
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.databinding.FragmentFeatureUserCashierCashOperationsDetailsBinding as ViewBinding
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.details.di.CashOperationsDetailsComponent
import javax.inject.Inject

internal class CashOperationsDetailsFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_cashier_cash_operations_details),
    IHasComponent<CashOperationsDetailsComponent>, CashOperationsDetailsView {

    @Inject
    lateinit var lazyPresenter: Lazy<CashOperationsDetailsPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): CashOperationsDetailsComponent =
        CashOperationsDetailsComponent.create(XInjectionManager.findComponent())

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

            cashOperationDetailsNextButton.setOnClickListener {
                presenter.navigateToCashOperationsCreationScreen()
            }
        }
    }

    override fun onLoadingCashOperationsDetails() =
        binding.stateLayout.setToLoading()

    @SuppressLint("SetTextI18n")
    override fun onSuccessCashOperationsDetails(cashOperationsDetails: CashOperationsDetails) {

        binding.apply {
            cashOperationsDetailCompanyNameTextView.text =
                "${cashOperationsDetails.companyBusinessType} \"${cashOperationsDetails.companyName}\""
            cashOperationsDetailCompanyBranchNameTextView.text =
                cashOperationsDetails.companyBranchName
            cashOperationsDetailCompanyAddressTextView.text =
                cashOperationsDetails.companyAddress
            cashOperationsDetailsTotalCashValueTextView.text =
                cashOperationsDetails.totalCash.roundToString()
            cashOperationsDetailsTotalSaleValueTextView.text =
                cashOperationsDetails.totalSale?.roundToString() ?: "---"
            cashOperationsDetailsTotalSaleCashValueTextView.text =
                cashOperationsDetails.totalSaleCash?.roundToString() ?: "---"
            cashOperationsDetailsTotalRefundValueTextView.text =
                cashOperationsDetails.totalRefund?.roundToString() ?: "---"
            cashOperationsDetailsTotalRefundCashValueTextView.text =
                cashOperationsDetails.totalRefundCash?.roundToString() ?: "---"


            cashOperationsDetailsTotalAddedCashValueTextView.text =
                cashOperationsDetails.totalAddedCash.roundToString()
            cashOperationsDetailsTotalReturnedAddedCashvalueTextView.text =
                cashOperationsDetails.totalReturnedAddedCash.roundToString()

            cashOperationsDetailsTotalExpenseValueTextView.text =
                cashOperationsDetails.totalExpense.roundToString()
            cashOperationsDetailsTotalReturnedExpenseValueTextView.text =
                cashOperationsDetails.totalReturnedExpense.roundToString()


            cashOperationsDetailsLastEncashmentValueTextView.text =
                cashOperationsDetails.totalEncashment?.roundToString() ?: "---"
            cashOperationsDetailsCashValueTextView.text =
                cashOperationsDetails.totalAmount.roundToString()


            stateLayout.setToSuccess()
        }
    }

    override fun onFailureCashOperationsDetails(throwable: Throwable) =
        binding.stateLayout.setToError(throwable)

    companion object {

        fun newInstance() =
            CashOperationsDetailsFragment()
                .withArguments()
    }
}