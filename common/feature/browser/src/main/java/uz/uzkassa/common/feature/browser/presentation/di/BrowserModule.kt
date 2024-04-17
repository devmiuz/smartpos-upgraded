package uz.uzkassa.common.feature.browser.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.common.feature.browser.data.repository.BrowserRepository
import uz.uzkassa.common.feature.browser.data.repository.BrowserRepositoryImpl

@Module(
    includes = [BrowserModule.Binders::class]
)
internal object BrowserModule {

    @Module
    interface Binders {

        @Binds
        @BrowserScope
        fun bindBrowserRepository(
            impl: BrowserRepositoryImpl
        ): BrowserRepository
    }

}