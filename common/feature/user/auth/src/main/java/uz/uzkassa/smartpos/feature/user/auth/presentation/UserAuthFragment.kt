package uz.uzkassa.smartpos.feature.user.auth.presentation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.languageManager
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.auth.R
import uz.uzkassa.smartpos.feature.user.auth.presentation.di.UserAuthComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.auth.databinding.FragmentFeatureUserAuthBinding as ViewBinding

class UserAuthFragment : MvpAppCompatFragment(R.layout.fragment_feature_user_auth),
    IHasComponent<UserAuthComponent>, UserAuthView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<UserAuthPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    @Inject
    internal lateinit var navigatorHolder: NavigatorHolder
    private val supportAppNavigator by lazy {
        SupportAppNavigator(requireActivity(), childFragmentManager, binding.frameLayout.id)
    }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): UserAuthComponent =
        UserAuthComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply { stateLayout.setOnErrorButtonClickListener { presenter.getUser() } }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(supportAppNavigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onLoadingUser() =
        binding.stateLayout.setToLoading()

    override fun onUserLanguageDefined(language: Language) =
        languageManager.changeLanguage(language)

    override fun onSuccessUser() =
        binding.stateLayout.setToSuccess()

    override fun onErrorUser(throwable: Throwable) =
        binding.stateLayout.setToError(throwable)

    companion object {

        fun newInstance() =
            UserAuthFragment().withArguments()
    }
}