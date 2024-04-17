package uz.uzkassa.smartpos.trade.presentation.global.features.users.runner

import ru.terrakok.cicerone.Screen
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureRunner

interface UsersSetupFeatureRunner : FeatureRunner {

    fun run(branchId: Long, action: (Screen) -> Unit)

    fun back(action: () -> Unit): UsersSetupFeatureRunner

    fun finish(action: () -> Unit): UsersSetupFeatureRunner
}