package uz.uzkassa.smartpos.feature.launcher.presentation.features.category

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.launcher.domain.category.CategorySetupInteractor
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.LauncherRouter
import javax.inject.Inject

internal class CategorySetupPresenter @Inject constructor(
    private val categorySetupInteractor: CategorySetupInteractor,
    private val launcherRouter: LauncherRouter
) : MvpPresenter<CategorySetupView>() {

    fun openCategorySetupScreen() {
        categorySetupInteractor
            .getCurrentBranch()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCurrentBranch() }
            .onSuccess {
                viewState.onSuccessCurrentBranch()
                launcherRouter.openCategorySetupScreen(it.id)
            }
            .onFailure { viewState.onErrorCurrentBranch(it) }
    }
}