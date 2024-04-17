package uz.uzkassa.smartpos.core.presentation.support.navigation

interface PlainNavigator {

    fun attach()

    fun detach()

    fun backTo(screen: PlainNavigationScreen)

    fun navigateTo(screen: PlainNavigationScreen)
}