package uz.uzkassa.smartpos.feature.check.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.user.model.UserResponse
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.utils.util.toString
import uz.uzkassa.smartpos.feature.check.manage.R
import uz.uzkassa.smartpos.feature.check.manage.databinding.FragmentReceiptCheckBinding
import uz.uzkassa.smartpos.feature.check.presentation.di.ReceiptCheckComponent
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.mapper.cashtransaction.mapToCashTransaction
import java.lang.StringBuilder
import javax.inject.Inject

class ReceiptCheckFragment : MvpAppCompatFragment(R.layout.fragment_receipt_check),
    IHasComponent<ReceiptCheckComponent>, ReceiptCheckView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<ReceiptCheckPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val binding: FragmentReceiptCheckBinding by viewBinding()
    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private var receipt: Receipt? = null
    override fun getComponent(): ReceiptCheckComponent =
        ReceiptCheckComponent.create(XInjectionManager.findComponent())

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

            qrScannerBtn.setOnClickListener {
                presenter.openQrCameraScannerScreen()
            }

            floatingActionButton.setOnClickListener {
                if (textInputEditText.text.toString().isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "No data", Toast.LENGTH_SHORT).show()
                } else {
                    presenter.getReceiptData(textInputEditText.text.toString())
                }
            }

            syncBtn.setOnClickListener { receipt?.let { it1 -> presenter.createReceipt(it1) } }
        }
    }

    companion object {

        fun newInstance() =
            ReceiptCheckFragment().withArguments()
    }

    override fun showQrScannerUrl(url: String) {
        binding.textInputEditText.setText(url)
    }

    override fun onLoadingReceiptDataByUrl() {
        binding.mainLayout.visibility = View.GONE
        loadingDialogDelegate.show()
    }

    override fun onSuccessReceiptDataByUrl(receipt: Receipt) {
        loadingDialogDelegate.dismiss()
        this.receipt = receipt
        binding.mainLayout.visibility = View.VISIBLE
        loadingDialogDelegate.dismiss()
        binding.kkmTv.text = receipt.terminalSerialNumber
        binding.smenaTv.text = "№ ${receipt.shiftNumber}"
        binding.receiptNumberTv.text =
            checkNotNull(receipt.receiptDate).toString("HH:mm dd.MM.yyyy")
        binding.allMoneyTv.text = "№ ${receipt.totalCost}"
//        presenter.getCompanyData(receipt.companyId)
        presenter.getCashierData(receipt.userId)
    }

    override fun onErrorReceiptDataByUrl(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, throwable)
        binding.mainLayout.visibility = View.GONE
    }

    override fun onSuccessCompanyData(companyResponse: CompanyResponse) {
        binding.stirTv.text = companyResponse.tin.toString()
        binding.registerDateTv.text = companyResponse.createdDate?.toString("dd-MM-yyyy")
    }

    override fun onErrorCompanyData(throwable: Throwable) {
        ErrorDialogFragment.show(this, throwable)
    }

    override fun onSuccessCashierData(userResponse: UserResponse) {
        binding.cashierTv.text =
            "${userResponse.fullName.firstName} ${userResponse.fullName.lastName}"
    }

    override fun onErrorUserData(throwable: Throwable) {
        ErrorDialogFragment.show(this, throwable)
    }

    override fun onLoadingCreateReceipt() {
        loadingDialogDelegate.show()
    }

    override fun onSuccessCreateReceipt() {
        loadingDialogDelegate.dismiss()
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
        presenter.backToRootScreen()
    }

    override fun onErrorCreateResponse(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
        ErrorDialogFragment.show(this, throwable)
    }
}