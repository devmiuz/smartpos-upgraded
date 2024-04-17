package uz.uzkassa.smartpos.trade.presentation.global.features.company.saving.runner

import ru.terrakok.cicerone.Screen

interface CompanySavingFeatureRunner {

    fun run(action: (Screen) -> Unit)

    fun finish(action: () -> Unit): CompanySavingFeatureRunner
}