package uz.uzkassa.smartpos.trade.presentation.global.features.user.list

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.list.dependencies.UserListFeatureArgs
import uz.uzkassa.smartpos.feature.user.list.dependencies.UserListFeatureCallback
import uz.uzkassa.smartpos.feature.user.list.presentation.UserListFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.user.list.runner.UserListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.saving.runner.UserSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class UserListFeatureMediator(
    private val router: Router,
    private val userSavingFeatureRunner: UserSavingFeatureRunner
) : FeatureMediator, UserListFeatureArgs, UserListFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    override var branchId: Long by Delegates.notNull()
    override var userRoleType: UserRole.Type by Delegates.notNull()

    val featureRunner: UserListFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenUserCreation() {
        userSavingFeatureRunner
            .back { router.backTo(Screens.UserListScreen) }
            .finish { router.backTo(Screens.UserListScreen) }
            .run(branchId, userRoleType) { router.navigateTo(it) }
    }

    override fun onOpenUserUpdate(userId: Long) {
        userSavingFeatureRunner
            .back { router.backTo(Screens.UserListScreen) }
            .finish { router.backTo(Screens.UserListScreen) }
            .run(branchId, userRoleType, userId) { router.navigateTo(it) }
    }

    override fun onBackFromUserList() =
        backAction.invoke()

    private inner class FeatureRunnerImpl : UserListFeatureRunner {

        override fun run(branchId: Long, userRoleType: UserRole.Type, action: (Screen) -> Unit) {
            this@UserListFeatureMediator.branchId = branchId
            this@UserListFeatureMediator.userRoleType = userRoleType
            action.invoke(Screens.UserListScreen)
        }

        override fun back(action: () -> Unit): UserListFeatureRunner {
            backAction = action
            return this
        }
    }

    private object Screens {

        object UserListScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                UserListFragment.newInstance()
        }
    }
}