package uz.uzkassa.smartpos.feature.launcher.presentation.features.language

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.presentation.utils.app.languageManager
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.launcher.R
import uz.uzkassa.smartpos.feature.launcher.presentation.features.language.delegate.RecyclerViewDelegate
import uz.uzkassa.smartpos.feature.launcher.presentation.features.language.di.LanguageSelectionComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.launcher.databinding.FragmentFeatureLauncherLanguageSelectionBinding as ViewBinding

internal class LanguageSelectionFragment
    : MvpAppCompatFragment(R.layout.fragment_feature_launcher_language_selection),
    IHasComponent<LanguageSelectionComponent>, LanguageSelectionView {

    @Inject
    lateinit var lazyPresenter: Lazy<LanguageSelectionPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()
    private val recyclerViewDelegate by lazy {
        RecyclerViewDelegate(this) { presenter.setLanguage(it) }
    }

    override fun getComponent(): LanguageSelectionComponent =
        LanguageSelectionComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            recyclerViewDelegate.onCreate(recyclerView, savedInstanceState)
        }
    }

    override fun onLanguagesDefined(languages: List<Language>) =
        recyclerViewDelegate.addAll(languages)

    override fun onLanguageChanged(language: Language) =
        languageManager.changeLanguage(language)

    companion object {

        fun newInstance() =
            LanguageSelectionFragment().withArguments()
    }
}