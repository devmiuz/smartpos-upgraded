package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.language.runner

import ru.terrakok.cicerone.Screen

interface UserLanguageChangeFeatureRunner {

    fun run(userId: Long, action: (Screen) -> Unit)

    fun back(action: () -> Unit): UserLanguageChangeFeatureRunner
}