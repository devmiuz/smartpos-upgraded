package uz.uzkassa.smartpos.feature.launcher.presentation.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import uz.uzkassa.smartpos.core.presentation.constants.GlobalConstants
import uz.uzkassa.smartpos.feature.launcher.R

@Suppress("unused")
internal class LauncherBackgroundLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val frameLayout: FrameLayout?
    private val textTextView: AppCompatTextView
    private val versionTextView: AppCompatTextView

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.widget_feature_launcher_launcherbackgroundlayout, this, true)

        frameLayout =
            findViewById(R.id.widget_feature_launcher_launcherbackgroundlayout_framelayout)
        textTextView = findViewById(android.R.id.text1)
        versionTextView = findViewById(android.R.id.text2)

        val obtainStyledAttributes: TypedArray =
            getContext().obtainStyledAttributes(attrs, R.styleable.LauncherBackgroundLayout)

        val textString: String? =
            obtainStyledAttributes.getString(R.styleable.LauncherBackgroundLayout_android_text)
        val useLayoutHierarchy: Boolean =
            obtainStyledAttributes.getBoolean(
                R.styleable.LauncherBackgroundLayout_use_layout_hierarchy,
                true
            )

        textTextView.apply { if (textString != null) text = textString else visibility = View.GONE }
        versionTextView.text = context.getString(
            R.string.core_presentation_crm_app_version,
            GlobalConstants.appVersion
        )

        frameLayout.layoutParams =
            (frameLayout.layoutParams as RelativeLayout.LayoutParams)
                .also { if (!useLayoutHierarchy) it.removeRule(RelativeLayout.BELOW) }
        obtainStyledAttributes.recycle()
    }

    override fun addView(child: View?) {
        frameLayout?.addView(child) ?: super.addView(child)
    }

    override fun addView(child: View?, index: Int) {
        frameLayout?.addView(child, index) ?: super.addView(child, index)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        frameLayout?.addView(child, params) ?: super.addView(child, params)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        frameLayout?.addView(child, width, height) ?: super.addView(child, width, height)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        frameLayout?.addView(child, index, params) ?: super.addView(child, index, params)
    }

    fun setText(@StringRes resId: Int) =
        with(textTextView) { visibility = View.VISIBLE; setText(resId) }

    @Suppress("UsePropertyAccessSyntax")
    fun setText(text: CharSequence) =
        with(textTextView) { visibility = View.VISIBLE; setText(text) }

    fun setText(@StringRes resId: Int, type: TextView.BufferType) =
        with(textTextView) { visibility = View.VISIBLE; setText(resId, type) }

    fun setText(text: CharSequence, type: TextView.BufferType) =
        with(textTextView) { visibility = View.VISIBLE; setText(text, type) }

    fun setText(text: CharArray, start: Int, len: Int) =
        with(textTextView) { visibility = View.VISIBLE; setText(text, start, len) }
}