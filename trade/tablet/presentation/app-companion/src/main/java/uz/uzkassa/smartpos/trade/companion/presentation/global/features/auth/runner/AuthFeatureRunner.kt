package uz.uzkassa.smartpos.trade.companion.presentation.global.features.auth.runner

import ru.terrakok.cicerone.Screen

interface AuthFeatureRunner {

    fun run(action: (Screen) -> Unit)

    fun finish(action: () -> Unit): AuthFeatureRunner
}