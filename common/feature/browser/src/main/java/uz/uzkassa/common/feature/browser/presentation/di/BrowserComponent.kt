package uz.uzkassa.common.feature.browser.presentation.di

import dagger.Component
import uz.uzkassa.common.feature.browser.dependencies.BrowserFeatureDependecies
import uz.uzkassa.common.feature.browser.presentation.BrowserFragment

@BrowserScope
@Component(
    dependencies = [BrowserFeatureDependecies::class],
    modules = [BrowserModule::class]
)
abstract class BrowserComponent : BrowserFeatureDependecies {

    internal abstract fun inject(fragment: BrowserFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: BrowserFeatureDependecies
        ): BrowserComponent
    }

    internal companion object {

        fun create(dependencies: BrowserFeatureDependecies): BrowserComponent =
            DaggerBrowserComponent
                .factory()
                .create(dependencies)
    }
}