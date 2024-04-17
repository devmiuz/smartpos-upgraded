package uz.uzkassa.smartpos.feature.user.confirmation.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.NavigatorHolder
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.di.UserConfirmationComponent
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.navigation.navigator.SupportAppNavigator
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.confirmation.databinding.FragmentFeatureUserConfirmationBinding as ViewBinding

class UserConfirmationFragment : MvpAppCompatDialogFragment(),
    IHasComponent<UserConfirmationComponent>, UserConfirmationView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<UserConfirmationPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    private val supportAppNavigator by lazy { SupportAppNavigator(this, binding.frameLayout.id) }

    private val delegate by lazy { DialogFragmentSupportDelegate(this) }
    private lateinit var binding: ViewBinding

    override fun getComponent(): UserConfirmationComponent =
        UserConfirmationComponent.create(XInjectionManager.findComponent())

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        delegate.onCreateDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewBinding.inflate(inflater, container, false).also { binding = it }.root

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
            UserConfirmationFragment().withArguments()
    }
}