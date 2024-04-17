package uz.uzkassa.smartpos.trade.presentation.global.features.users

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.users.setup.dependencies.UsersSetupFeatureArgs
import uz.uzkassa.smartpos.feature.users.setup.dependencies.UsersSetupFeatureCallback
import uz.uzkassa.smartpos.feature.users.setup.presentation.UsersSetupFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.user.saving.runner.UserSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.users.runner.UsersSetupFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import javax.inject.Inject
import kotlin.properties.Delegates

class UsersSetupFeatureMediator @Inject constructor(
    private val router: Router,
    private val userSavingFeatureMediator: UserSavingFeatureRunner
) : FeatureMediator, UsersSetupFeatureArgs, UsersSetupFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    private var finishAction: (() -> Unit) by Delegates.notNull()
    override var branchId: Long by Delegates.notNull()

    val featureRunner: UsersSetupFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenUserCreation(branchId: Long) {
        userSavingFeatureMediator
            .back { router.backTo(Screens.UsersSetupScreen) }
            .finish { router.backTo(Screens.UsersSetupScreen) }
            .run(branchId, UserRole.Type.OWNER) { router.navigateTo(it) }
    }

    override fun onOpenUserUpdateScreen(branchId: Long, userId: Long) {
        userSavingFeatureMediator
            .back { router.backTo(Screens.UsersSetupScreen) }
            .finish { router.backTo(Screens.UsersSetupScreen) }
            .run(branchId, UserRole.Type.OWNER, userId) { router.navigateTo(it) }
    }

    override fun onBackFromUsersSetup() =
        backAction.invoke()

    override fun onFinishUsersSetup() =
        finishAction.invoke()

    private inner class FeatureRunnerImpl : UsersSetupFeatureRunner {

        override fun run(branchId: Long, action: (Screen) -> Unit) {
            this@UsersSetupFeatureMediator.branchId = branchId
            action.invoke(Screens.UsersSetupScreen)
        }

        override fun back(action: () -> Unit): UsersSetupFeatureRunner {
            backAction = action
            return this
        }

        override fun finish(action: () -> Unit): UsersSetupFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object UsersSetupScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                UsersSetupFragment.newInstance()
        }
    }
}