package uz.uzkassa.smartpos.trade.presentation.global

import moxy.MvpPresenter
import uz.uzkassa.smartpos.trade.presentation.global.coordinators.FeaturesCoordinator
import javax.inject.Inject

class GlobalPresenter @Inject constructor(
    private val featuresCoordinator: FeaturesCoordinator
): MvpPresenter<GlobalView>() {

    override fun onFirstViewAttach() =
        featuresCoordinator.launch()
}