package uz.uzkassa.smartpos.trade.presentation.global.features.account.recovery.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface AccountRecoveryPasswordFeatureRunner : FeatureRunner {

    fun run(phoneNumber: String, action: (Screen) -> Unit)

    fun back(action: () -> Unit): AccountRecoveryPasswordFeatureRunner

    fun finish(action: () -> Unit): AccountRecoveryPasswordFeatureRunner
}