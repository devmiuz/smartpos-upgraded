package uz.uzkassa.smartpos.feature.user.settings.password.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.settings.password.data.repository.UserAuthRepository
import uz.uzkassa.smartpos.feature.user.settings.password.data.repository.UserAuthRepositoryImpl

@Module(includes = [UserPasswordChangeModule.Binders::class])
internal object UserPasswordChangeModule {

    @Module
    interface Binders {

        @Binds
        @UserPasswordChangeScope
        fun bindUserAuthRepository(impl: UserAuthRepositoryImpl): UserAuthRepository
    }
}