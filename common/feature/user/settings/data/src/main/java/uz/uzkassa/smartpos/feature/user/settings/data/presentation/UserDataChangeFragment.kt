package uz.uzkassa.smartpos.feature.user.settings.data.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.presentation.utils.widget.setTextChangedListener
import uz.uzkassa.smartpos.feature.user.settings.data.R
import uz.uzkassa.smartpos.feature.user.settings.data.presentation.di.UserDataChangeComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.settings.data.databinding.FragmentFeatureUserDataChangeBinding as ViewBinding

class UserDataChangeFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_data_change),
    IHasComponent<UserDataChangeComponent>, UserDataChangeView,
    androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<UserDataChangePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }

    override fun getComponent(): UserDataChangeComponent =
        UserDataChangeComponent.create(XInjectionManager.findComponent())

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

            lastNameTextInputEditText.setTextChangedListener(this@UserDataChangeFragment) {
                lastNameTextInputLayout.apply { if (error != null) error = null }
                presenter.setUserLastName(it.toString())
            }

            firstNameTextInputEditText.setTextChangedListener(this@UserDataChangeFragment) {
                firstNameTextInputLayout.apply { if (error != null) error = null }
                presenter.setUserName(it.toString())
            }

            patronymicTextInputEditText.setTextChangedListener(this@UserDataChangeFragment) {
                patronymicTextInputLayout.apply { if (error != null) error = null }
                presenter.setPatronymic(it.toString())
            }

            stateLayout.setOnErrorButtonClickListener { presenter.getUser() }
        }
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.menu_user_settings_save_data_menuitem ->
            presenter.changeDataChanges().let { true }
        else -> false
    }

    override fun onLoadingUser() =
        binding.stateLayout.setToLoading()

    override fun onSuccessUser(user: User) {
        toolbarDelegate.apply {
            clearMenu()
            inflateMenu(R.menu.menu_feature_user_settings_data, this@UserDataChangeFragment)
            setTintMenuItemById(
                menuResId = R.id.menu_user_settings_save_data_menuitem,
                colorResId = requireContext().colorAccent
            )
        }

        binding.apply {
            if (!presenter.isInRestoreState(this@UserDataChangeFragment)) {
                with(user) {
                    firstNameTextInputEditText.setText(fullName.firstName)
                    lastNameTextInputEditText.setText(fullName.lastName)
                    patronymicTextInputEditText.setText(fullName.patronymic)
                }
            }

            stateLayout.setToSuccess()
        }
    }

    override fun onErrorUser(throwable: Throwable) =
        binding.stateLayout.setToError(throwable)

    override fun onLoadingChange() =
        loadingDialogDelegate.show()

    override fun onSuccessChange() =
        loadingDialogDelegate.dismiss()

    override fun onErrorChangeCauseFirstNameNotDefined() {
        loadingDialogDelegate.dismiss()
        binding.firstNameTextInputLayout.error =
            getString(R.string.fragment_feature_user_data_change_name_empty_error)
    }

    override fun onErrorChangeCauseLastNameNotDefined() {
        loadingDialogDelegate.dismiss()
        binding.lastNameTextInputLayout.error =
            getString(R.string.fragment_feature_user_data_change_last_name_empty_error)
    }

    override fun onErrorChange(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            UserDataChangeFragment().withArguments()
    }
}