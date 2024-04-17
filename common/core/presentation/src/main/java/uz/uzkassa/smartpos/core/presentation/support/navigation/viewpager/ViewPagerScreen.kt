package uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager

import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainNavigationScreen

abstract class ViewPagerScreen : PlainNavigationScreen {

    open fun getScreenPosition(): Int? = null

    open fun smoothScroll(): Boolean = true
}