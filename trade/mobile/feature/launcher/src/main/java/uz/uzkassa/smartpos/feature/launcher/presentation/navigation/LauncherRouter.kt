package uz.uzkassa.smartpos.feature.launcher.presentation.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.launcher.data.model.launcher.LauncherState
import uz.uzkassa.smartpos.feature.launcher.data.model.sync.SyncState
import uz.uzkassa.smartpos.feature.launcher.dependencies.LauncherFeatureCallback
import uz.uzkassa.smartpos.feature.launcher.presentation.features.account.AccountAuthFragment
import uz.uzkassa.smartpos.feature.launcher.presentation.features.category.CategorySetupFragment
import uz.uzkassa.smartpos.feature.launcher.presentation.features.language.LanguageSelectionFragment
import uz.uzkassa.smartpos.feature.launcher.presentation.features.sync.SyncFragment
import uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth.UserAuthFragment
import uz.uzkassa.smartpos.feature.launcher.presentation.features.user.creation.UserCreationFragment

internal class LauncherRouter(
    private val launcherFeatureCallback: LauncherFeatureCallback,
    private val router: Router
) {

    fun openRootScreen(launcherState: LauncherState) = when (launcherState) {
        LauncherState.ACCOUNT_AUTH_SELECTION ->
            router.newRootScreen(Screens.LauncherAccountAuthScreen())
        LauncherState.ACCOUNT_LOGIN_COMPLETE, LauncherState.SYNC, LauncherState.USER_AUTH,
        LauncherState.USER_CREATION ->
            router.newRootScreen(Screens.SyncScreen())
        LauncherState.LANGUAGE_SELECTION ->
            router.newRootScreen(Screens.LanguageSelectionScreen())
        LauncherState.COMPANY_CREATION ->
            openCompanyCreationScreen()
        LauncherState.CATEGORY_SETUP ->
            router.newRootScreen(Screens.LauncherProductCatalogSetupScreen())
    }

    fun openRootScreen(syncState: SyncState) = when {
        !syncState.isUsersCreated ->
            router.newRootScreen(Screens.UserCreationScreen())
        !syncState.isCategoriesDefined ->
            router.newRootScreen(Screens.LauncherProductCatalogSetupScreen())
        else -> router.newRootScreen(Screens.LauncherUserAuthScreen())
    }

    fun openUserCreationScreen(branchId: Long) =
        launcherFeatureCallback.openUserCreation(branchId)

    fun openAccountAuthScreen(hasBeenAuthorized: Boolean) {
        launcherFeatureCallback.openAccountAuth(hasBeenAuthorized)
    }

    fun openAccountAuthRegistrationScreen() {
        launcherFeatureCallback.openAccountAuthRegistration()
    }

    fun openCategorySetupScreen(branchId: Long) {
        launcherFeatureCallback.openCategorySetup(branchId)
    }

    fun openCompanyCreationScreen() {
        launcherFeatureCallback.openCompanyCreation()
    }

    fun openBranchSelectionSetupScreen() {
        launcherFeatureCallback.openBranchSelectionSetup()
    }

    fun openUserAuthScreen(branchId: Long, userId: Long, userRoleType: UserRole.Type) {
        launcherFeatureCallback.openUserAuth(branchId, userId, userRoleType)
    }

    private object Screens {

        class LanguageSelectionScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                LanguageSelectionFragment.newInstance()
        }

        class LauncherAccountAuthScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                AccountAuthFragment.newInstance()
        }

        class LauncherProductCatalogSetupScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CategorySetupFragment.newInstance()
        }

        class SyncScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                SyncFragment.newInstance()
        }

        class LauncherUserAuthScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                UserAuthFragment.newInstance()
        }

        class UserCreationScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                UserCreationFragment.newInstance()
        }
    }
}