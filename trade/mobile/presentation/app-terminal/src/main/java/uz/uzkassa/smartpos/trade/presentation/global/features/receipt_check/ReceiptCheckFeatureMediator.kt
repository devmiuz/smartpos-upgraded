package uz.uzkassa.smartpos.trade.presentation.global.features.receipt_check

import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.sendBlocking
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureArgs
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureCallback
import uz.uzkassa.smartpos.feature.check.presentation.ReceiptCheckFragment
import uz.uzkassa.smartpos.feature.check.presentation.features.QrScannerFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.list.BranchListFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving.runner.BranchSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.receipt_check.runner.ReceiptCheckFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class ReceiptCheckFeatureMediator(
    private val router: Router
) : FeatureMediator, ReceiptCheckFeatureCallback, ReceiptCheckFeatureArgs {
    private var backAction: (() -> Unit) by Delegates.notNull()
    private var finishAction: ((branchId: Long) -> Unit)? = null
    override var branchId: Long by Delegates.notNull()
    override var userRoleType: UserRole.Type by Delegates.notNull()
    override val qrUrlBroadcastChannel = BroadcastChannelWrapper<String>()

    val featureRunner: ReceiptCheckFeatureRunner = FeatureRunnerImpl()

    private inner class FeatureRunnerImpl : ReceiptCheckFeatureRunner {

        override fun run(branchId: Long, userRoleType: UserRole.Type, action: (Screen) -> Unit) {
            this@ReceiptCheckFeatureMediator.branchId = branchId
            this@ReceiptCheckFeatureMediator.userRoleType = userRoleType
            action.invoke(Screens.ReceiptCheckScreen)
        }

        override fun back(action: () -> Unit): ReceiptCheckFeatureRunner {
            backAction = action
            return this
        }

        override fun finish(action: (branchId: Long) -> Unit): ReceiptCheckFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object ReceiptCheckScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                ReceiptCheckFragment.newInstance()
        }

        object QrScannerScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                QrScannerFragment.newInstance()
        }
    }

    override fun onBackFromCheckReceipt() = router.exit()

    override fun openQrScannerScreen() =
        featureRunner
            .back { router.backTo(Screens.ReceiptCheckScreen) }
            .finish { router.backTo(Screens.ReceiptCheckScreen) }
            .run { router.navigateTo(Screens.QrScannerScreen) }

    override fun backToReceiptCheckScreen(url:String) {
        qrUrlBroadcastChannel.sendBlocking(url)
        router.backTo(Screens.ReceiptCheckScreen)
    }
}