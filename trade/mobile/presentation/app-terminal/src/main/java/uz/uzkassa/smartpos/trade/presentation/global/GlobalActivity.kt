package uz.uzkassa.smartpos.trade.presentation.global

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.NavigatorHolder
import uz.uzkassa.smartpos.core.presentation.app.activity.ActivitySupport
import uz.uzkassa.smartpos.core.presentation.app.activity.ActivitySupportDelegate
import uz.uzkassa.smartpos.core.presentation.app.dispatchers.keyevent.OnKeyEventDispatcher
import uz.uzkassa.smartpos.core.presentation.support.locale.LanguageManager
import uz.uzkassa.smartpos.core.utils.util.toString
import uz.uzkassa.smartpos.feature.receipt.remote.presentation.ReceiptRemoteLifecycleDelegate
import uz.uzkassa.smartpos.trade.BuildConfig
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalComponent
import uz.uzkassa.smartpos.trade.presentation.global.navigation.navigator.GlobalNavigator
import java.util.*
import javax.inject.Inject
import uz.uzkassa.smartpos.trade.databinding.ActivityGlobalBinding as ViewBinding

class GlobalActivity : MvpAppCompatActivity(), IHasComponent<GlobalComponent>,
    ActivitySupport, GlobalView {

    @Inject
    lateinit var delegate: ActivitySupportDelegate

    @Inject
    lateinit var lazyPresenter: Lazy<GlobalPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    private val globalNavigator by lazy { GlobalNavigator(this, binding.frameLayout.id) }

    @Inject
    lateinit var receiptRemoteLifecycleDelegate: ReceiptRemoteLifecycleDelegate

    override val languageManager: LanguageManager
        get() = delegate.languageManager

    override val onKeyEventDispatcher: OnKeyEventDispatcher
        get() = delegate.onKeyEventDispatcher

    private val binding: ViewBinding by lazy { ViewBinding.inflate(layoutInflater) }

    override fun getComponent(): GlobalComponent =
        GlobalComponent.create(this, XInjectionManager.findComponent())

    override fun attachBaseContext(newBase: Context) {
        XInjectionManager.bindComponent(this).inject(this)
        super.attachBaseContext(delegate.withBaseContext(newBase))
    }

    override fun applyOverrideConfiguration(configuration: Configuration?) =
        super.applyOverrideConfiguration(delegate.withConfiguration(baseContext, configuration))

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(delegate.withOnCreate(savedInstanceState))
        setContentView(binding.root)
        delegate.onCreate(this)
        if (BuildConfig.DEBUG) binding.debugInfoTextView.let {
            it.visibility = View.VISIBLE
            it.text = "Debug: ${BuildConfig.VERSION_NAME} " +
                    "(${Date(BuildConfig.BUILD_DATE).toString("dd.MM.yyyy")})"
        }
    }

    override fun onStart() {
        super.onStart()
        delegate.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        receiptRemoteLifecycleDelegate.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        delegate.onStop()
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean =
        if (onKeyEventDispatcher.dispatchKeyEvent(event)) true
        else super.dispatchKeyEvent(event)

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(globalNavigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}