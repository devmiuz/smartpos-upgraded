package uz.uzkassa.smartpos.trade.auth.presentation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.auth.R
import uz.uzkassa.smartpos.trade.auth.presentation.delegate.QRCodeImageViewDelegate
import uz.uzkassa.smartpos.trade.auth.presentation.di.AuthComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.auth.databinding.FragmentFeatureAuthBinding as ViewBinding


class AuthFragment : MvpAppCompatFragment(R.layout.fragment_feature_auth),
    IHasComponent<AuthComponent>, AuthView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<AuthPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val qrCodeImageViewDelegate by lazy { QRCodeImageViewDelegate(this) }

    override fun getComponent(): AuthComponent =
        AuthComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        qrCodeImageViewDelegate.onCreate(binding.authQrCodeImageView, savedInstanceState)
    }

    override fun onAuthCodeDefined(code: String) {
        qrCodeImageViewDelegate.generateQrCode(code)
    }

    override fun onLoadingCodeCheck() =
        loadingDialogDelegate.show()

    override fun onSuccessCodeCheck() =
        loadingDialogDelegate.dismiss()

    override fun onFailureCodeCheck(throwable: Throwable) {
        loadingDialogDelegate.dismiss()
    }

    companion object {

        fun newInstance() =
            AuthFragment().withArguments()
    }
}