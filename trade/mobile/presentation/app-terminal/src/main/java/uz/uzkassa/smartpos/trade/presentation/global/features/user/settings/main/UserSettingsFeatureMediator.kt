package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.main

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.feature.user.settings.dependencies.UserSettingsFeatureArgs
import uz.uzkassa.smartpos.feature.user.settings.dependencies.UserSettingsFeatureCallback
import uz.uzkassa.smartpos.feature.user.settings.presentation.UserSettingsFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.data.runner.UserDataChangeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.language.runner.UserLanguageChangeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.main.runner.UserSettingsFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.password.runner.UserPasswordChangeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.phone_number.runner.UserPhoneNumberChangeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class UserSettingsFeatureMediator(
    private val router: Router,
    private val userDataChangeFeatureRunner: UserDataChangeFeatureRunner,
    private val userLanguageChangeFeatureRunner: UserLanguageChangeFeatureRunner,
    private val userPasswordChangeFeatureRunner: UserPasswordChangeFeatureRunner,
    private val userPhoneNumberChangeFeatureRunner: UserPhoneNumberChangeFeatureRunner
) : FeatureMediator, UserSettingsFeatureArgs, UserSettingsFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    override var userId: Long by Delegates.notNull()
    override var userRoleType: UserRole.Type by Delegates.notNull()

    val featureRunner: UserSettingsFeatureRunner =
        FeatureRunnerImpl()

    override fun onBackFromUserSettings() =
        backAction.invoke()

    override fun onOpenLanguageChangeScreen() {
        userLanguageChangeFeatureRunner
            .back { router.backTo(Screens.UserSettingsScreen) }
            .run(userId) { router.navigateTo(it) }
    }

    override fun onOpenPersonalDataChangeScreen() {
        userDataChangeFeatureRunner
            .back { router.backTo(Screens.UserSettingsScreen) }
            .run(userId) { router.navigateTo(it) }
    }

    override fun onOpenPasswordChangingScreen() {
        userPasswordChangeFeatureRunner.run(userId) { router.navigateTo(it) }
    }

    override fun onOpenPhoneNumberChangingScreen() {
        userPhoneNumberChangeFeatureRunner
            .run(userId) { router.navigateTo(it) }
    }

    private inner class FeatureRunnerImpl : UserSettingsFeatureRunner {
        override fun run(userId: Long, userRoleType: UserRole.Type, action: (Screen) -> Unit) {
            this@UserSettingsFeatureMediator.userId = userId
            this@UserSettingsFeatureMediator.userRoleType = userRoleType
            action.invoke(Screens.UserSettingsScreen)
        }

        override fun back(action: () -> Unit): UserSettingsFeatureRunner {
            backAction = action
            return this
        }
    }

    private object Screens {

        object UserSettingsScreen : SupportAppScreen() {
            override fun getFragment(): Fragment? =
                UserSettingsFragment.newInstance()
        }
    }
}