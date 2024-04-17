package uz.uzkassa.smartpos.trade.presentation.global.features.account.auth.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.core.data.source.resource.account.model.Account
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface AccountAuthFeatureRunner : FeatureRunner {

    fun run(action: (Screen) -> Unit)

    fun back(action: () -> Unit): AccountAuthFeatureRunner

    fun finish(action: (Account) -> Unit): AccountAuthFeatureRunner
}