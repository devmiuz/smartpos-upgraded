package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.creation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.launcher.R
import uz.uzkassa.smartpos.feature.launcher.presentation.features.user.creation.di.UserCreationComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.launcher.databinding.FragmentFeatureLauncherUserAdminCreationBinding as ViewBinding

internal class UserCreationFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_launcher_user_admin_creation),
    IHasComponent<UserCreationComponent>, UserCreationView {

    @Inject
    lateinit var lazyPresenter: Lazy<UserCreationPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): UserCreationComponent =
        UserCreationComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            skipUserCreationButton.setOnClickListener { presenter.skipUserCreation() }
            createUserButton.setOnClickListener { presenter.openUserCreationScreen() }
        }
    }

    override fun onLoadingDetails() =
        loadingDialogDelegate.show()

    override fun onSuccessDetails() {
        loadingDialogDelegate.dismiss()
        binding.apply {
            buttonPanel.visibility = View.VISIBLE
            launcherBackgroundLayout.setText(
                getString(R.string.fragment_feature_launcher_user_creation_text)
            )
        }
    }

    override fun onErrorDetails(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            UserCreationFragment().withArguments()
    }
}