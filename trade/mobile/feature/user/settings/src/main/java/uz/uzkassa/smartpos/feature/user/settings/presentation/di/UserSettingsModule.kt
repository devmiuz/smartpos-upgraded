package uz.uzkassa.smartpos.feature.user.settings.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.settings.data.repository.UserRepository
import uz.uzkassa.smartpos.feature.user.settings.data.repository.UserRepositoryImpl

@Module(
    includes = [UserSettingsModule.Binders::class]
)
internal object UserSettingsModule {

    @Module
    interface Binders {

        @Binds
        @UserSettingsScope
        fun bindUserDataRepository(
            impl: UserRepositoryImpl
        ): UserRepository
    }
}