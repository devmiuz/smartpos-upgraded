package uz.uzkassa.smartpos.feature.user.auth.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.auth.data.repository.shift.ShiftReportRepository
import uz.uzkassa.smartpos.feature.user.auth.data.repository.shift.ShiftReportRepositoryImpl
import uz.uzkassa.smartpos.feature.user.auth.data.repository.user.UserRepository
import uz.uzkassa.smartpos.feature.user.auth.data.repository.user.UserRepositoryImpl

@Module(includes = [UserAuthModule.Binders::class])
internal object UserAuthModule {

    @Module
    interface Binders {

        @Binds
        @UserAuthScope
        fun bindUserRepository(
            impl: UserRepositoryImpl
        ): UserRepository

        @Binds
        @UserAuthScope
        fun bindShiftReportRepository(
            impl: ShiftReportRepositoryImpl
        ): ShiftReportRepository
    }
}