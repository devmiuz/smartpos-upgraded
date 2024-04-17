package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.password.runner

import ru.terrakok.cicerone.Screen

interface UserPasswordChangeFeatureRunner {

    fun run(userId: Long, action: (Screen) -> Unit)
}