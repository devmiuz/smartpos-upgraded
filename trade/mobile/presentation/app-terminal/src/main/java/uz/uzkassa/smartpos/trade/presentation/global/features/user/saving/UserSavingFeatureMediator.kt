package uz.uzkassa.smartpos.trade.presentation.global.features.user.saving

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.saving.dependencies.UserSavingFeatureArgs
import uz.uzkassa.smartpos.feature.user.saving.dependencies.UserSavingFeatureCallback
import uz.uzkassa.smartpos.feature.user.saving.presentation.creation.UserCreationFragment
import uz.uzkassa.smartpos.feature.user.saving.presentation.update.UserUpdateFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.user.saving.runner.UserSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class UserSavingFeatureMediator : FeatureMediator, UserSavingFeatureArgs,
    UserSavingFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    private var finishAction: (() -> Unit)? = null
    override var branchId: Long by Delegates.notNull()
    override var userId: Long? = null
    override var userRoleType: UserRole.Type by Delegates.notNull()

    val featureRunner: UserSavingFeatureRunner =
        FeatureRunnerImpl()

    override fun onBackFromUserSaving() =
        backAction.invoke()

    override fun onFinishUserSaving() {
        finishAction?.invoke()
    }

    private inner class FeatureRunnerImpl : UserSavingFeatureRunner {

        override fun run(
            branchId: Long,
            userRoleType: UserRole.Type,
            userId: Long?,
            action: (Screen) -> Unit
        ) {
            this@UserSavingFeatureMediator.branchId = branchId
            this@UserSavingFeatureMediator.userId = userId
            this@UserSavingFeatureMediator.userRoleType = userRoleType
            val screen: Screen =
                if (userId == null) Screens.UserCreationScreen
                else Screens.UserUpdateScreen
            action.invoke(screen)
        }

        override fun back(action: () -> Unit): UserSavingFeatureRunner {
            backAction = action
            return this
        }

        override fun finish(action: () -> Unit): UserSavingFeatureRunner {
            finishAction = action
            return this
        }

    }

    private object Screens {

        object UserCreationScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                UserCreationFragment.newInstance()
        }

        object UserUpdateScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                UserUpdateFragment.newInstance()
        }
    }
}