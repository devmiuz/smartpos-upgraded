package uz.uzkassa.smartpos.feature.launcher.presentation

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.launcher.R
import uz.uzkassa.smartpos.feature.launcher.presentation.di.LauncherComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.launcher.databinding.FragmentFeatureLauncherBinding as ViewBinding

class LauncherFragment : MvpAppCompatFragment(R.layout.fragment_feature_launcher),
    IHasComponent<LauncherComponent>, LauncherView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<LauncherPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    @Inject
    internal lateinit var navigatorHolder: NavigatorHolder
    private val supportAppNavigator by lazy {
        SupportAppNavigator(requireActivity(), childFragmentManager, binding.frameLayout.id)
    }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): LauncherComponent =
        LauncherComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(supportAppNavigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    companion object {

        fun newInstance() =
            LauncherFragment().withArguments()
    }
}