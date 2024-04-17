package uz.uzkassa.smartpos.feature.user.auth.presentation.features.unsupported

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.auth.R
import uz.uzkassa.smartpos.feature.user.auth.presentation.features.unsupported.di.UnsupportedAuthComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.auth.databinding.FragmentFeatureUserAuthUnsupportedBinding as ViewBinding

internal class UnsupportedAuthFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_auth_unsupported),
    IHasComponent<UnsupportedAuthComponent>, UnsupportedAuthView {

    @Inject
    lateinit var lazyPresenter: Lazy<UnsupportedAuthPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): UnsupportedAuthComponent =
        UnsupportedAuthComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigationOnClickListener { presenter.backToRootScreen() }
            }
        }
    }

    companion object {

        fun newInstance() =
            UnsupportedAuthFragment().withArguments()
    }
}