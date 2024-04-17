package uz.uzkassa.smartpos.core.presentation.app.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import uz.uzkassa.smartpos.core.data.source.resource.language.preference.LanguagePreference
import uz.uzkassa.smartpos.core.presentation.app.activity.anr.AnrSupervisorKt
import uz.uzkassa.smartpos.core.presentation.app.dispatchers.keyevent.OnKeyEventDispatcher
import uz.uzkassa.smartpos.core.presentation.support.locale.LanguageManager
import uz.uzkassa.smartpos.core.presentation.support.locale.LocaleManager
import uz.uzkassa.smartpos.core.presentation.support.locale.delegate.LocaleManagerActivityDelegate

class ActivitySupportDelegate(private val languagePreference: LanguagePreference) {
    private var delegate: LocaleManagerActivityDelegate? = null

    val languageManager: LanguageManager by lazy { LanguageManager(checkNotNull(delegate)) }

    val onKeyEventDispatcher: OnKeyEventDispatcher = OnKeyEventDispatcher()

    fun onCreate(activitySupport: ActivitySupport) {
        delegate = LocaleManagerActivityDelegate(activitySupport, languagePreference)
    }

    fun onStart() =
        AnrSupervisorKt.start()

    fun onStop() =
        AnrSupervisorKt.stop()

    fun withBaseContext(newBase: Context) =
        LocaleManager.updateConfiguration(newBase, languagePreference.language.locale)

    fun withConfiguration(
        baseContext: Context,
        overrideConfiguration: Configuration?
    ): Configuration? =
        delegate?.applyOverrideConfiguration(baseContext, overrideConfiguration)

    fun withOnCreate(savedInstanceState: Bundle?) =
        savedInstanceState?.apply { putSerializable("android:support:fragments", null) }
}