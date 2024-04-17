package uz.uzkassa.smartpos.core.presentation.app.callback

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import uz.uzkassa.smartpos.core.presentation.app.activity.ActivitySupport
import uz.uzkassa.smartpos.core.presentation.utils.app.hideSoftInput

internal class ApplicationLifecycle private constructor(application: Application) {

    init {
        application.registerActivityLifecycleCallbacks(ActivitySupportCallback)
    }

    private object ActivitySupportCallback : Application.ActivityLifecycleCallbacks {

        override fun onActivityPreStarted(activity: Activity) {
            if (activity !is ActivitySupport)
                throw RuntimeException("Unable to start non-support activity")
        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                activity.window.decorView.importantForAutofill =
                    View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS

            if (activity is FragmentActivity)
                activity.supportFragmentManager
                    .registerFragmentLifecycleCallbacks(FragmentSupportCallback, true)
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }
    }

    private object FragmentSupportCallback : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) =
            f.hideSoftInput()
    }

    companion object {

        fun registerCallbacks(application: Application) {
            ApplicationLifecycle(application)
        }
    }
}