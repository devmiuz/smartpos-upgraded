package uz.uzkassa.smartpos.trade.presentation.global.di

import dagger.Module
import uz.uzkassa.smartpos.trade.presentation.global.features.account.auth.di.AccountAuthFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.account.recovery.di.AccountRecoveryPasswordFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.account.registration.di.AccountRegistrationFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.activitytype.di.ActivityTypeSelectionFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.delete.di.BranchDeleteFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.list.di.BranchListFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.saving.di.BranchSavingFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.branch.selection_setup.di.BranchSelectionSetupFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.browser.di.BrowserFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.apay.qrcode.di.CashierApayFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.cashtransaction.di.CashierOperationsFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.refund.di.CashierReceiptRefundFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.cashier.sale.di.CashierSaleFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.category.list.di.CategoryListFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.category.main.di.MainCategoriesFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.category.saving.di.CategorySavingFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.category.selection.di.CategorySelectionFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.category.setup.di.CategorySetupFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.category.type.di.CategoryTypeFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.company.saving.di.CompanySavingFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.company.vat.di.CompanyVATSelectionFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.amount.di.PaymentAmountFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.discount.di.PaymentDiscountFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.quantity.di.ProductQuantityFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.launcher.di.LauncherFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.marking.di.ProductMarkingFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.product.list.di.ProductListFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.product.saving.di.ProductSavingFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.product.unit.creation.di.ProductUnitCreationFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.receipt.di.ReceiptRemoteFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.receipt_check.di.ReceiptCheckFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.region_city.di.RegionCitySelectionFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.supervisor.dashboard.di.SupervisorDashboardFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.user.auth.di.UserAuthFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.user.autoprint.di.AutoPrintFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.user.confirmation.di.UserConfirmationFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.user.list.di.UserListFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.user.saving.di.UserSavingFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.data.di.UserDataChangeFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.language.di.UserLanguageChangeFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.main.di.UserSettingsFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.password.di.UserPasswordChangeFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.user.settings.phone_number.di.UserPhoneNumberChangeFeatureMediatorModule
import uz.uzkassa.smartpos.trade.presentation.global.features.users.di.UsersSetupFeatureMediatorModule

@Module(
    includes = [
        AccountAuthFeatureMediatorModule::class,
        AccountRecoveryPasswordFeatureMediatorModule::class,
        AccountRegistrationFeatureMediatorModule::class,
        ActivityTypeSelectionFeatureMediatorModule::class,
        BranchDeleteFeatureMediatorModule::class,
        BranchListFeatureMediatorModule::class,
        BranchSavingFeatureMediatorModule::class,
        BranchSelectionSetupFeatureMediatorModule::class,
        BrowserFeatureMediatorModule::class,
        CashierApayFeatureMediatorModule::class,
        CashierOperationsFeatureMediatorModule::class,
        CashierReceiptRefundFeatureMediatorModule::class,
        CashierSaleFeatureMediatorModule::class,
        CategoryListFeatureMediatorModule::class,
        CategorySavingFeatureMediatorModule::class,
        CategorySelectionFeatureMediatorModule::class,
        CategorySetupFeatureMediatorModule::class,
        CategoryTypeFeatureMediatorModule::class,
        CompanySavingFeatureMediatorModule::class,
        CompanyVATSelectionFeatureMediatorModule::class,
        LauncherFeatureMediatorModule::class,
        MainCategoriesFeatureMediatorModule::class,
        PaymentAmountFeatureMediatorModule::class,
        PaymentDiscountFeatureMediatorModule::class,
        ReceiptCheckFeatureMediatorModule::class,
        ReceiptRemoteFeatureMediatorModule::class,
        ProductMarkingFeatureMediatorModule::class,
        ProductListFeatureMediatorModule::class,
        ProductUnitCreationFeatureMediatorModule::class,
        ProductSavingFeatureMediatorModule::class,
        ProductQuantityFeatureMediatorModule::class,
        RegionCitySelectionFeatureMediatorModule::class,
        SupervisorDashboardFeatureMediatorModule::class,
        UserAuthFeatureMediatorModule::class,
        UserConfirmationFeatureMediatorModule::class,
        UserPasswordChangeFeatureMediatorModule::class,
        UserPhoneNumberChangeFeatureMediatorModule::class,
        UserDataChangeFeatureMediatorModule::class,
        UserLanguageChangeFeatureMediatorModule::class,
        UserListFeatureMediatorModule::class,
        UserSavingFeatureMediatorModule::class,
        UsersSetupFeatureMediatorModule::class,
        UserSettingsFeatureMediatorModule::class,
        AutoPrintFeatureMediatorModule::class
    ]
)
object GlobalModuleFeatureMediators