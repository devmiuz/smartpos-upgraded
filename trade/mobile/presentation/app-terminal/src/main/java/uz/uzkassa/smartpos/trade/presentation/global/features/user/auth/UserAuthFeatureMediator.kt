package uz.uzkassa.smartpos.trade.presentation.global.features.user.auth

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureArgs
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureCallback
import uz.uzkassa.smartpos.feature.user.auth.presentation.UserAuthFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.account.recovery.runner.AccountRecoveryPasswordFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.auth.runner.UserAuthFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class UserAuthFeatureMediator(
    private val accountRecoveryPasswordFeatureRunner: AccountRecoveryPasswordFeatureRunner,
    private val router: Router
) : FeatureMediator, UserAuthFeatureArgs, UserAuthFeatureCallback {
    private var cashierAuthAction: ((userId: Long, userRoleType: UserRole.Type) -> Unit) by Delegates.notNull()
    private var supervisorAuthAction: ((userId: Long, userRoleType: UserRole.Type) -> Unit) by Delegates.notNull()
    private var backAction: (() -> Unit) by Delegates.notNull()
    override var userId: Long by Delegates.notNull(); private set
    override var userRoleType: UserRole.Type by Delegates.notNull(); private set

    val featureRunner: UserAuthFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenCashierUser(userId: Long, userRoleType: UserRole.Type) =
        cashierAuthAction.invoke(userId, userRoleType)

    override fun onOpenSupervisorUser(userId: Long, userRoleType: UserRole.Type) =
        supervisorAuthAction.invoke(userId, userRoleType)

    override fun onOpenRecoveryPassword(phoneNumber: String) {
        accountRecoveryPasswordFeatureRunner
            .back { router.exit() }
            .finish { router.backTo(Screens.UserAuthScreen) }
            .run(phoneNumber) { router.navigateTo(it) }
    }

    override fun onBackFromUserAuth() =
        backAction.invoke()

    inner class FeatureRunnerImpl : UserAuthFeatureRunner {

        override fun run(
            userId: Long,
            userRoleType: UserRole.Type,
            action: (Screen) -> Unit
        ) {
            this@UserAuthFeatureMediator.userId = userId
            this@UserAuthFeatureMediator.userRoleType = userRoleType
            action.invoke(Screens.UserAuthScreen)
        }

        override fun openCashierAuth(
            action: (userId: Long, userRoleType: UserRole.Type) -> Unit
        ): UserAuthFeatureRunner {
            cashierAuthAction = action
            return this
        }

        override fun openSupervisorAuth(
            action: (userId: Long, userRoleType: UserRole.Type) -> Unit
        ): UserAuthFeatureRunner {
            supervisorAuthAction = action
            return this
        }

        override fun back(action: () -> Unit): UserAuthFeatureRunner {
            backAction = action
            return this
        }
    }

    private object Screens {

        object UserAuthScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                UserAuthFragment.newInstance()
        }
    }
}