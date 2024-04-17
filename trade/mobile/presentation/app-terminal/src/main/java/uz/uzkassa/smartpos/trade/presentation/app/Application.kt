package uz.uzkassa.smartpos.trade.presentation.app

import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import com.nexgo.common.LogUtils
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import uz.uzkassa.smartpos.core.manager.logger.Logger
import uz.uzkassa.smartpos.core.manager.logger.impl.FileLoggerTree
import uz.uzkassa.smartpos.core.presentation.app.application.ApplicationSupport
import uz.uzkassa.smartpos.core.presentation.app.application.ApplicationSupportDelegate
import uz.uzkassa.smartpos.core.presentation.constants.GlobalConstants
import uz.uzkassa.smartpos.trade.BuildConfig
import uz.uzkassa.smartpos.trade.presentation.app.di.ApplicationComponent
import uz.uzkassa.smartpos.trade.presentation.app.worker.WorkManagerDelegate
import uz.uzkassa.smartpos.trade.presentation.support.dagger2.worker.WorkerProviderFactory
import javax.inject.Inject
import kotlin.properties.Delegates
import android.app.Application as AndroidApplication
import androidx.work.Configuration as WorkConfiguration

class Application : AndroidApplication(), IHasComponent<ApplicationComponent>,
    ApplicationSupport, WorkConfiguration.Provider {

    @Inject
    lateinit var delegate: ApplicationSupportDelegate

    @Inject
    lateinit var workerProviderFactory: WorkerProviderFactory

    private var base: Context by Delegates.notNull()

    override fun getComponent(): ApplicationComponent =
        ApplicationComponent.create(applicationSupport = this, context = base)

    override fun getWorkManagerConfiguration(): WorkConfiguration =
        WorkConfiguration.Builder()
            .setWorkerFactory(workerProviderFactory)
            .build()

    override fun attachBaseContext(base: Context) {
        this.base = base
        MultiDex.install(base)
        Logger.init(FileLoggerTree(base))
        XInjectionManager.let { it.init(this); it.bindComponent(this).inject(this) }
        delegate.attachBaseContext(base)
    }

    override fun onSuperAttachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        GlobalConstants.appVersion = BuildConfig.VERSION_NAME
        delegate.onCreate()
        WorkManagerDelegate.enqueue(this)
        LogUtils.setDebugEnable(true)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        delegate.onConfigurationChanged()
    }
}