package uz.uzkassa.smartpos.core.presentation.support.navigation

import java.util.concurrent.CopyOnWriteArrayList

open class PlainRouter<Navigator : PlainNavigator>(val navigator: Navigator) {
    private var observers: MutableList<NavigatorObserver> = CopyOnWriteArrayList()

    open fun addNavigatorObserver(observer: NavigatorObserver) {
        if (!observers.contains(observer))
            observers.add(observer)
    }

    open fun removeNavigatorObserver(observer: NavigatorObserver) {
        if (observers.contains(observer))
            observers.remove(observer)
    }

    open fun backTo(screen: PlainNavigationScreen) {
        observers.forEach { it.onObserveNavigation(screen) }
        navigator.backTo(screen)
    }

    open fun navigateTo(screen: PlainNavigationScreen) {
        observers.forEach { it.onObserveNavigation(screen) }
        navigator.navigateTo(screen)
    }

    interface NavigatorObserver {
        fun onObserveNavigation(screen: PlainNavigationScreen)
    }
}