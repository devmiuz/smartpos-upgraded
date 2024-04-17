package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.data.runner

import ru.terrakok.cicerone.Screen

interface UserDataChangeFeatureRunner {

    fun run(userId: Long, action: (Screen) -> Unit)

    fun back(action: () -> Unit): UserDataChangeFeatureRunner
}