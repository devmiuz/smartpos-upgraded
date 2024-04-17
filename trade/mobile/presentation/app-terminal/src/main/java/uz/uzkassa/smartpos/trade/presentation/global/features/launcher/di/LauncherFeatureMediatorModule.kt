package uz.uzkassa.smartpos.trade.presentation.global.features.launcher.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.launcher.dependencies.LauncherFeatureArgs
import uz.uzkassa.smartpos.feature.launcher.dependencies.LauncherFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.account.auth.runner.AccountAuthFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.account.registration.runner.AccountRegistrationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.selection_setup.runner.BranchSelectionSetupFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.category.setup.runner.CategorySetupFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.company.saving.runner.CompanySavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.launcher.LauncherFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.launcher.runner.LauncherFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.user.auth.runner.UserAuthFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.users.runner.UsersSetupFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        LauncherFeatureMediatorModule.Binders::class,
        LauncherFeatureMediatorModule.Providers::class
    ]
)
object LauncherFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindLauncherFeatureArgs(
            launcherFeatureMediator: LauncherFeatureMediator
        ): LauncherFeatureArgs

        @Binds
        @GlobalScope
        fun bindLauncherFeatureCallback(
            launcherFeatureMediator: LauncherFeatureMediator
        ): LauncherFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideLauncherFeatureRunner(
            launcherFeatureMediator: LauncherFeatureMediator
        ): LauncherFeatureRunner =
            launcherFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideLauncherFeatureMediator(
            accountAuthFeatureRunner: AccountAuthFeatureRunner,
            accountRegistrationFeatureRunner: AccountRegistrationFeatureRunner,
            branchSelectionSetupFeatureRunner: BranchSelectionSetupFeatureRunner,
            categorySetupFeatureRunner: CategorySetupFeatureRunner,
            companySavingFeatureMediator: CompanySavingFeatureRunner,
            globalRouter: GlobalRouter,
            userAuthFeatureRunner: UserAuthFeatureRunner,
            usersSetupFeatureMediator: UsersSetupFeatureRunner
        ): LauncherFeatureMediator =
            LauncherFeatureMediator(
                accountAuthFeatureRunner = accountAuthFeatureRunner,
                accountRegistrationFeatureRunner = accountRegistrationFeatureRunner,
                branchSelectionSetupFeatureRunner = branchSelectionSetupFeatureRunner,
                categorySetupFeatureRunner = categorySetupFeatureRunner,
                companySavingFeatureRunner = companySavingFeatureMediator,
                router = globalRouter,
                userAuthFeatureRunner = userAuthFeatureRunner,
                usersSetupFeatureRunner = usersSetupFeatureMediator
            )
    }
}