package uz.uzkassa.smartpos.feature.user.auth.presentation.features.supervisor.dagger2

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.auth.data.repository.supervisor.SupervisorAuthRepository
import uz.uzkassa.smartpos.feature.user.auth.data.repository.supervisor.SupervisorAuthRepositoryImpl

@Module(includes = [SupervisorAuthModule.Binders::class])
internal object SupervisorAuthModule {

    @Module
    interface Binders {

        @Binds
        @SupervisorAuthScope
        fun bindSupervisorAuthRepository(
            impl: SupervisorAuthRepositoryImpl
        ): SupervisorAuthRepository
    }
}