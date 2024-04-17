package uz.uzkassa.smartpos.feature.user.autoprint.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.autoprint.data.repository.AutoPrintRepository
import uz.uzkassa.smartpos.feature.user.autoprint.data.repository.AutoPrintRepositoryImpl

@Module(includes = [AutoPrintModule.Binders::class])
internal object AutoPrintModule {

    @Module
    interface Binders {

        @Binds
        @AutoPrintScope
        fun bindAutoPrintRepository(
            impl: AutoPrintRepositoryImpl
        ): AutoPrintRepository
    }
}