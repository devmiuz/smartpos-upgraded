package uz.uzkassa.smartpos.trade.presentation.global.features.supervisor.dashboard.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureArgs
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.list.runner.BranchListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.category.type.runner.CategoryTypeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.receipt_check.runner.ReceiptCheckFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.supervisor.dashboard.SupervisorDashboardFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.supervisor.dashboard.runner.SupervisorDashboardFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.list.runner.UserListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.main.runner.UserSettingsFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        SupervisorDashboardFeatureMediatorModule.Binders::class,
        SupervisorDashboardFeatureMediatorModule.Providers::class
    ]
)
object SupervisorDashboardFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindSupervisorDashboardFeatureArgs(
            supervisorDashboardFeatureMediator: SupervisorDashboardFeatureMediator
        ): SupervisorDashboardFeatureArgs

        @Binds
        @GlobalScope
        fun bindSupervisorDashboardFeatureCallback(
            supervisorDashboardFeatureMediator: SupervisorDashboardFeatureMediator
        ): SupervisorDashboardFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideSupervisorDashboardFeatureRunner(
            supervisorDashboardFeatureMediator: SupervisorDashboardFeatureMediator
        ): SupervisorDashboardFeatureRunner =
            supervisorDashboardFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideSupervisorDashboardFeatureMediator(
            receiptCheckFeatureRunner: ReceiptCheckFeatureRunner,
            branchListFeatureRunner: BranchListFeatureRunner,
            categoryTypeFeatureRunner: CategoryTypeFeatureRunner,
            globalRouter: GlobalRouter,
            userListFeatureRunner: UserListFeatureRunner,
            userSettingsFeatureRunner: UserSettingsFeatureRunner
        ): SupervisorDashboardFeatureMediator =
            SupervisorDashboardFeatureMediator(
                receiptCheckFeatureRunner = receiptCheckFeatureRunner,
                branchListFeatureRunner = branchListFeatureRunner,
                categoryTypeFeatureRunner = categoryTypeFeatureRunner,
                router = globalRouter,
                userListFeatureRunner = userListFeatureRunner,
                userSettingsFeatureRunner = userSettingsFeatureRunner
            )
    }
}