package uz.uzkassa.smartpos.trade.companion.presentation.app

import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import uz.uzkassa.smartpos.core.presentation.app.application.ApplicationSupport
import uz.uzkassa.smartpos.core.presentation.app.application.ApplicationSupportDelegate
import uz.uzkassa.smartpos.trade.companion.presentation.app.di.ApplicationComponent
import javax.inject.Inject
import kotlin.properties.Delegates
import android.app.Application as AndroidApplication

class Application : AndroidApplication(), IHasComponent<ApplicationComponent>,
    ApplicationSupport {

    @Inject
    lateinit var delegate: ApplicationSupportDelegate

    private var base: Context by Delegates.notNull()

    override fun getComponent(): ApplicationComponent =
        ApplicationComponent.create(applicationSupport = this, context = base)

    override fun attachBaseContext(base: Context) {
        this.base = base
        MultiDex.install(base)
        XInjectionManager.let { it.init(this); it.bindComponent(this).inject(this) }
        delegate.attachBaseContext(base)
    }

    override fun onSuperAttachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        delegate.onCreate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        delegate.onConfigurationChanged()
    }
}