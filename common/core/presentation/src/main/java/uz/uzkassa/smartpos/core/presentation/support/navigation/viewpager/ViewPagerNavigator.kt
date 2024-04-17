package uz.uzkassa.smartpos.core.presentation.support.navigation.viewpager

import androidx.annotation.CallSuper
import androidx.viewpager.widget.ViewPager
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ViewPagerDelegate
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainNavigationScreen
import uz.uzkassa.smartpos.core.presentation.support.navigation.PlainNavigator
import java.lang.ref.WeakReference

open class ViewPagerNavigator : PlainNavigator {
    private var viewReference: WeakReference<ViewPager?>? = null

    override fun attach() =
        throw UnsupportedOperationException()

    open fun attachViewPager(viewPager: ViewPager) {
        viewReference = WeakReference(viewPager)
    }

    open fun attachViewPager(viewPagerDelegate: ViewPagerDelegate) {
        viewReference = WeakReference(viewPagerDelegate.view)
    }

    @CallSuper
    override fun detach() {
        viewReference?.clear()
        viewReference = null
    }

    override fun backTo(screen: PlainNavigationScreen) {
        val viewPagerScreen: ViewPagerScreen = checkScreen(screen)
        val position: Int = getScreenPosition(viewPagerScreen)
        viewReference?.get()?.setCurrentItem(position, viewPagerScreen.smoothScroll())
    }

    override fun navigateTo(screen: PlainNavigationScreen) {
        val viewPagerScreen: ViewPagerScreen = checkScreen(screen)
        val position: Int? = checkScreenPosition(viewPagerScreen.getScreenPosition())
        viewReference?.get()
            ?.apply { setCurrentItem(position ?: currentItem + 1, viewPagerScreen.smoothScroll()) }
    }

    protected open fun checkScreen(screen: PlainNavigationScreen): ViewPagerScreen =
        if (screen !is ViewPagerScreen) throw RuntimeException("Can't create a screen: $screen")
        else screen

    protected open fun checkScreenPosition(position: Int?): Int? =
        if (position == 0) throw RuntimeException("Scroll screen position can't be a 0")
        else position?.minus(1)

    protected open fun getScreenPosition(screen: ViewPagerScreen): Int {
        val screenPosition: Int =
            screen.getScreenPosition() ?: throw RuntimeException("Can't create a screen: $screen")
        return checkNotNull(checkScreenPosition(screenPosition))
    }
}