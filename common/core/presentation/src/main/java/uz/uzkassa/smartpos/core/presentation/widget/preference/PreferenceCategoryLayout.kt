package uz.uzkassa.smartpos.core.presentation.widget.preference

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.presentation.utils.content.colorPrimary
import uz.uzkassa.smartpos.core.presentation.utils.content.colorPrimaryDark

class PreferenceCategoryLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val titleTextView: AppCompatTextView

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.widget_preference_preferencecategorylayout, this, true)

        orientation = VERTICAL

        titleTextView = findViewById(android.R.id.title)

        val attributes: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.PreferenceCategoryLayout, 0, 0
        )

        if (attributes.hasValue(R.styleable.PreferenceCategoryLayout_preference_category_title)) {
            titleTextView.text =
                attributes.getString(R.styleable.PreferenceCategoryLayout_preference_category_title)

            if (attributes.hasValue(R.styleable.PreferenceCategoryLayout_preference_category_title_padding)) {
                val paddingDimension = attributes.getDimensionPixelSize(
                    R.styleable.PreferenceCategoryLayout_preference_category_title_padding,
                    0
                )
                titleTextView.setPadding(
                    paddingDimension,
                    paddingDimension,
                    paddingDimension,
                    paddingDimension
                )
            } else {
                if (attributes.hasValue(R.styleable.PreferenceCategoryLayout_preference_category_title_padding_horizontal)) {
                    val paddingDimension = attributes.getDimensionPixelSize(
                        R.styleable.PreferenceCategoryLayout_preference_category_title_padding_horizontal,
                        0
                    )
                    titleTextView.setPadding(
                        paddingDimension,
                        paddingTop,
                        paddingDimension,
                        paddingBottom
                    )
                }

                if (attributes.hasValue(R.styleable.PreferenceCategoryLayout_preference_category_title_padding_vertical)) {
                    val paddingDimension = attributes.getDimensionPixelSize(
                        R.styleable.PreferenceCategoryLayout_preference_category_title_padding_vertical,
                        0
                    )
                    titleTextView.setPadding(
                        paddingLeft,
                        paddingDimension,
                        paddingRight,
                        paddingDimension
                    )
                }
            }

            if (attributes.hasValue(R.styleable.PreferenceCategoryLayout_preference_category_title_tint)) {
                val colorResId: Int = attributes.getResourceId(
                    R.styleable.PreferenceCategoryLayout_preference_category_title_tint,
                    0
                )
                titleTextView.setTextColor(ContextCompat.getColor(context, colorResId))
            } else if (attributes.hasValue(R.styleable.PreferenceCategoryLayout_preference_category_use_theme_title_tint)) {
                val colorType: Int = attributes.getInt(
                    R.styleable.PreferenceCategoryLayout_preference_category_use_theme_title_tint,
                    -1
                )

                if (colorType != -1) {
                    when (colorType) {
                        0 -> titleTextView.setTextColor(context.colorAccent)
                        1 -> titleTextView.setTextColor(context.colorPrimary)
                        2 -> titleTextView.setTextColor(context.colorPrimaryDark)
                    }
                }
            }
        }

        attributes.recycle()
    }
}