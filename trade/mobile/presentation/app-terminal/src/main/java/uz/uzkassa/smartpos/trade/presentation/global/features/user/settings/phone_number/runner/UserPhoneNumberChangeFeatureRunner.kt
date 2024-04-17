package uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.phone_number.runner

import ru.terrakok.cicerone.Screen

interface UserPhoneNumberChangeFeatureRunner {

    fun run(userId: Long, action: (Screen) -> Unit)
}