package uz.uzkassa.smartpos.trade.presentation.global.features.account.registration.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface AccountRegistrationFeatureRunner : FeatureRunner {

    fun run(action: (Screen) -> Unit)

    fun back(action: () -> Unit): AccountRegistrationFeatureRunner

    fun finish(action: () -> Unit): AccountRegistrationFeatureRunner
}