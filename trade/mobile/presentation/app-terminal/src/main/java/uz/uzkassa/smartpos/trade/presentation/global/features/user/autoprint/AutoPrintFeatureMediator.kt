package uz.uzkassa.smartpos.trade.presentation.global.features.user.autoprint

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.autoprint.dependencies.AutoPrintFeatureArgs
import uz.uzkassa.smartpos.feature.user.autoprint.dependencies.AutoPrintFeatureCallback
import uz.uzkassa.smartpos.feature.user.autoprint.presentation.AutoPrintFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.user.autoprint.runner.AutoPrintFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class AutoPrintFeatureMediator(
    private val router: Router
): FeatureMediator, AutoPrintFeatureArgs, AutoPrintFeatureCallback {
    override var userId: Long by Delegates.notNull(); private set
    override var branchId: Long by Delegates.notNull(); private set
    override var userRoleType: UserRole.Type by Delegates.notNull(); private set

    val featureRunner: AutoPrintFeatureRunner  = FeatureRunnerImpl()
    private var backAction: (() -> Unit) by Delegates.notNull()
    inner class FeatureRunnerImpl : AutoPrintFeatureRunner {

        override fun run(
            branchId:Long,
            userId: Long,
            userRoleType: UserRole.Type,
            action: (Screen) -> Unit
        ) {
            this@AutoPrintFeatureMediator.branchId = branchId
            this@AutoPrintFeatureMediator.userId = userId
            this@AutoPrintFeatureMediator.userRoleType = userRoleType
            action.invoke(Screens.AutoPrintScreen)
        }

        override fun back(action: () -> Unit): AutoPrintFeatureRunner {
            backAction = action
            return this
        }
    }

    private object Screens {

        object AutoPrintScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                AutoPrintFragment.newInstance()
        }
    }

    override fun onBack() {
        backAction.invoke()
    }
}