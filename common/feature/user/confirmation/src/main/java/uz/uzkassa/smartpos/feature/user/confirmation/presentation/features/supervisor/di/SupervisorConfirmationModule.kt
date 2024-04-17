package uz.uzkassa.smartpos.feature.user.confirmation.presentation.features.supervisor.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.SupervisorConfirmationRepository
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.SupervisorConfirmationRepositoryImpl
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.admin.BranchAdminConfirmationRepository
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.admin.BranchAdminConfirmationRepositoryImpl
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.owner.OwnerConfirmationRepository
import uz.uzkassa.smartpos.feature.user.confirmation.data.repository.owner.OwnerConfirmationRepositoryImpl

@Module(includes = [SupervisorConfirmationModule.Binders::class])
internal object SupervisorConfirmationModule {

    @Module
    interface Binders {

        @Binds
        @SupervisorConfirmationScope
        fun bindBranchAdminConfirmationRepository(
            impl: BranchAdminConfirmationRepositoryImpl
        ): BranchAdminConfirmationRepository

        @Binds
        @SupervisorConfirmationScope
        fun bindOwnerConfirmationRepository(
            impl: OwnerConfirmationRepositoryImpl
        ): OwnerConfirmationRepository

        @Binds
        @SupervisorConfirmationScope
        fun bindSupervisorConfirmationRepository(
            impl: SupervisorConfirmationRepositoryImpl
        ): SupervisorConfirmationRepository
    }
}