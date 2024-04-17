package uz.uzkassa.smartpos.core.presentation.support.delegate.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager.widget.ViewPager.PageTransformer
import uz.uzkassa.smartpos.core.presentation.widget.viewpager.ViewPager

@Suppress("unused", "MemberVisibilityCanBePrivate")
open class ViewPagerDelegate(target: Fragment) : ViewDelegate<ViewPager>(target) {
    var isPagingEnabled: Boolean
        get() = view?.isPagingEnabled ?: false
        set(value) {
            view?.isPagingEnabled = value
        }

    val currentPosition: Int
        get() = view?.currentItem ?: -1

    val isFirstPosition: Boolean
        get() = currentPosition == 0

    val isNotFirstPosition: Boolean
        get() = !isFirstPosition

    val isLastPosition: Boolean
        get() = currentPosition == adapterSize - 1

    val adapterSize: Int
        get() = view?.adapter?.count ?: 0

    fun onCreate(view: ViewPager, adapter: PagerAdapter, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        setAdapter(adapter)
    }

    fun onCreate(
        view: ViewPager,
        adapter: PagerAdapter,
        isPagingEnabled: Boolean,
        savedInstanceState: Bundle?
    ) {
        onCreate(view, adapter, savedInstanceState)
        this.isPagingEnabled = isPagingEnabled
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("", ReplaceWith("throw UnsupportedOperationException()"))
    final override fun onCreate(view: ViewPager, savedInstanceState: Bundle?) =
        throw UnsupportedOperationException()

    @Suppress("UNCHECKED_CAST")
    fun <T : PagerAdapter> getAdapter(): T? =
        view?.adapter as? T?

    @Suppress("UsePropertyAccessSyntax")
    fun setAdapter(pagerAdapter: PagerAdapter) {
        view?.setAdapter(pagerAdapter)
    }

    @Suppress("UsePropertyAccessSyntax")
    fun resetAdapter() {
        view?.setAdapter(null)
    }

    fun addOnPageChangeListener(onPageChangeListener: OnPageChangeListener) {
        view?.addOnPageChangeListener(onPageChangeListener)
    }

    fun removeOnPageChangeListener(onPageChangeListener: OnPageChangeListener) {
        view?.removeOnPageChangeListener(onPageChangeListener)
    }

    fun setPageTransformer(
        reverseDrawingOrder: Boolean = false,
        transformer: PageTransformer,
        pageLayerType: Int = View.LAYER_TYPE_HARDWARE
    ) {
        view?.setPageTransformer(reverseDrawingOrder, transformer, pageLayerType)
    }

    fun setCurrentPage(position: Int, smoothScroll: Boolean = true) {
        view?.setCurrentItem(position, smoothScroll)
    }

    fun slideToNextPage() {
        view?.let { it.currentItem += 1 }
    }

    fun slideToPreviousPage() {
        view?.let { it.currentItem -= 1 }
    }

    fun slideToFirstPage(smoothScroll: Boolean = true) {
        view?.setCurrentItem(0, smoothScroll)
    }

    fun slideToLastPage(smoothScroll: Boolean = true) {
        view?.setCurrentItem(adapterSize, smoothScroll)
    }
}