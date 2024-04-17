package uz.uzkassa.smartpos.trade.presentation.global.features.user.confirmation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureArgs
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureCallback
import uz.uzkassa.smartpos.feature.user.confirmation.presentation.UserConfirmationFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.user.confirmation.runner.UserConfirmationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class UserConfirmationFeatureMediator : FeatureMediator,
    UserConfirmationFeatureArgs, UserConfirmationFeatureCallback {
    private var finishAction: ((isConfirmed: Boolean) -> Unit) by Delegates.notNull()
    override var branchId: Long by Delegates.notNull()
    override var userRoleType: UserRole.Type by Delegates.notNull()

    val featureRunner: UserConfirmationFeatureRunner =
        FeatureRunnerImpl()

    override fun onFinishUserConfirmation(isConfirmed: Boolean) =
        finishAction.invoke(isConfirmed)

    private inner class FeatureRunnerImpl : UserConfirmationFeatureRunner {

        override fun run(branchId: Long, userRoleType: UserRole.Type, action: (Screen) -> Unit) {
            this@UserConfirmationFeatureMediator.branchId = branchId
            this@UserConfirmationFeatureMediator.userRoleType = userRoleType
            action.invoke(Screens.UserConfirmationScreen)
        }

        override fun finish(action: (isConfirmed: Boolean) -> Unit): UserConfirmationFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object UserConfirmationScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                UserConfirmationFragment.newInstance()
        }
    }
}