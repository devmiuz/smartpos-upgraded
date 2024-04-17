package uz.uzkassa.smartpos.core.presentation.widget.viewpager.transformations

import android.view.View
import androidx.viewpager.widget.ViewPager

class ViewPagerFadeOutTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.translationX = -position * page.width
        page.alpha = 1 - Math.abs(position)
    }
}