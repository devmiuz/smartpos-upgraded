package uz.uzkassa.smartpos.feature.user.settings.phonenumber.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.data.repository.UserDataRepository
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.data.repository.UserDataRepositoryImpl

@Module(includes = [UserPhoneNumberChangeModule.Binders::class])
internal object UserPhoneNumberChangeModule {

    @Module
    interface Binders {

        @Binds
        @UserPhoneNumberChangeScope
        fun bindUserDataRepository(
            impl: UserDataRepositoryImpl
        ): UserDataRepository
    }
}