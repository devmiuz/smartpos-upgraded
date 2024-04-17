package uz.uzkassa.smartpos.feature.launcher.presentation.features.language.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.launcher.data.repository.language.LanguageRepository
import uz.uzkassa.smartpos.feature.launcher.data.repository.language.LanguageRepositoryImpl

@Module(includes = [LanguageSelectionModule.Binders::class])
internal object LanguageSelectionModule {

    @Module
    interface Binders {

        @Binds
        @LanguageSelectionScope
        fun bindLanguageRepository(
            impl: LanguageRepositoryImpl
        ): LanguageRepository
    }
}