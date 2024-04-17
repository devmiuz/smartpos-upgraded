package uz.uzkassa.smartpos.feature.user.settings.language.presentation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.loading.LoadingDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.dispatchers.addCallback
import uz.uzkassa.smartpos.core.presentation.utils.app.languageManager
import uz.uzkassa.smartpos.core.presentation.utils.app.onBackPressedDispatcher
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.user.settings.language.R
import uz.uzkassa.smartpos.feature.user.settings.language.data.model.LanguageSelection
import uz.uzkassa.smartpos.feature.user.settings.language.presentation.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.user.settings.language.presentation.di.UserLanguageChangeComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.user.settings.language.databinding.FragmentFeatureUserLanguageChangeBinding as ViewBinding

class UserLanguageChangeFragment :
    MvpAppCompatFragment(R.layout.fragment_feature_user_language_change),
    IHasComponent<UserLanguageChangeComponent>, UserLanguageChangeView {

    @Inject
    internal lateinit var lazyPresenter: Lazy<UserLanguageChangePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val loadingDialogDelegate by lazy { LoadingDialogDelegate(this) }

    private val binding: ViewBinding by viewBinding()
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(this) { t1, t2 -> presenter.setLanguage(t1, t2) }
    }

    override fun getComponent(): UserLanguageChangeComponent =
        UserLanguageChangeComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this) { presenter.backToRootScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                setNavigation(R.drawable.core_presentation_vector_drawable_arrow_left)
                { onBackPressedDispatcher.onBackPressed() }
            }
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
            languageFloatingActionButton.setOnClickListener { presenter.changeUserLanguage() }
        }
    }

    override fun onLanguageSelectionChanged(languageSelection: LanguageSelection) {
        recyclerViewDelegate.update(languageSelection)
    }

    override fun onLoadingLanguages() =
        recyclerViewDelegate.onLoading()

    override fun onSuccessLanguages(languages: List<LanguageSelection>) {
        binding.languageFloatingActionButton.show()
        recyclerViewDelegate.onSuccess(languages)
    }

    override fun onErrorLanguages(throwable: Throwable) =
        recyclerViewDelegate.onFailure(throwable)

    override fun onLoadingChangeLanguage() =
        loadingDialogDelegate.show()

    override fun onSuccessChangeLanguage(language: Language) {
        loadingDialogDelegate.dismiss()
        languageManager.changeLanguage(language)
    }

    override fun onErrorChangeLanguage(throwable: Throwable) {
        loadingDialogDelegate.dismiss()

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            UserLanguageChangeFragment().withArguments()
    }
}