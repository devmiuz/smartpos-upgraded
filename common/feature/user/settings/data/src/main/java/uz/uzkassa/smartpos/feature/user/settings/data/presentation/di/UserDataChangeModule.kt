package uz.uzkassa.smartpos.feature.user.settings.data.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.settings.data.data.repository.UserDataRepository
import uz.uzkassa.smartpos.feature.user.settings.data.data.repository.UserDataRepositoryImpl

@Module(includes = [UserDataChangeModule.Binders::class])
internal object UserDataChangeModule {

    @Module
    interface Binders {

        @Binds
        @UserDataChangeScope
        fun bindUserDataRepository(
            impl: UserDataRepositoryImpl
        ): UserDataRepository
    }
}