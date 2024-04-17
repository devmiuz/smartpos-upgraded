package uz.uzkassa.smartpos.trade.presentation.global.di

import uz.uzkassa.common.feature.browser.dependencies.BrowserFeatureDependecies
import uz.uzkassa.common.feature.category.selection.dependencies.CategorySelectionFeatureDependencies
import uz.uzkassa.smartpos.feature.account.auth.dependencies.AccountAuthFeatureDependencies
import uz.uzkassa.smartpos.feature.account.recovery.password.dependencies.AccountRecoveryPasswordFeatureDependencies
import uz.uzkassa.smartpos.feature.account.registration.dependencies.AccountRegistrationFeatureDependencies
import uz.uzkassa.smartpos.feature.activitytype.selection.dependencies.ActivityTypeSelectionFeatureDependencies
import uz.uzkassa.apay.dependencies.CashierApayFeatureDependencies
import uz.uzkassa.smartpos.feature.branch.delete.dependencies.BranchDeleteFeatureDependencies
import uz.uzkassa.smartpos.feature.branch.list.dependencies.BranchListFeatureDependencies
import uz.uzkassa.smartpos.feature.branch.saving.dependencies.BranchSavingFeatureDependencies
import uz.uzkassa.smartpos.feature.branch.selection.setup.dependencies.BranchSelectionSetupFeatureDependencies
import uz.uzkassa.smartpos.feature.category.list.dependencies.CategoryListFeatureDependencies
import uz.uzkassa.smartpos.feature.category.main.dependencies.MainCategoriesFeatureDependencies
import uz.uzkassa.smartpos.feature.category.saving.dependencies.CategorySavingFeatureDependencies
import uz.uzkassa.smartpos.feature.category.selection.setup.dependencies.CategorySetupFeatureDependencies
import uz.uzkassa.smartpos.feature.category.type.dependencies.CategoryTypeFeatureDependencies
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureDependencies
import uz.uzkassa.smartpos.feature.company.saving.dependencies.CompanySavingFeatureDependencies
import uz.uzkassa.smartpos.feature.company.vat.selection.dependencies.CompanyVATSelectionFeatureDependencies
import uz.uzkassa.smartpos.feature.helper.payment.amount.dependencies.PaymentAmountFeatureDependencies
import uz.uzkassa.smartpos.feature.helper.payment.discount.dependencies.PaymentDiscountFeatureDependencies
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureDependencies
import uz.uzkassa.smartpos.feature.launcher.dependencies.LauncherFeatureDependencies
import uz.uzkassa.smartpos.feature.product.list.dependencies.ProductListFeatureDependencies
import uz.uzkassa.smartpos.feature.product.saving.dependencies.ProductSavingFeatureDependencies
import uz.uzkassa.smartpos.feature.product.unit.creation.dependencies.ProductUnitCreationFeatureDependencies
import uz.uzkassa.smartpos.feature.product_marking.dependencies.ProductMarkingFeatureDependencies
import uz.uzkassa.smartpos.feature.receipt.remote.dependencies.ReceiptRemoteFeatureDependencies
import uz.uzkassa.smartpos.feature.regioncity.selection.dependencies.RegionCitySelectionFeatureDependencies
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureDependencies
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureDependencies
import uz.uzkassa.smartpos.feature.user.autoprint.dependencies.AutoPrintFeatureDependencies
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies.CashierCashOperationsFeatureDependencies
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureDependencies
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureDependencies
import uz.uzkassa.smartpos.feature.user.confirmation.dependencies.UserConfirmationFeatureDependencies
import uz.uzkassa.smartpos.feature.user.list.dependencies.UserListFeatureDependencies
import uz.uzkassa.smartpos.feature.user.saving.dependencies.UserSavingFeatureDependencies
import uz.uzkassa.smartpos.feature.user.settings.data.dependencies.UserDataChangeFeatureDependencies
import uz.uzkassa.smartpos.feature.user.settings.dependencies.UserSettingsFeatureDependencies
import uz.uzkassa.smartpos.feature.user.settings.language.dependencies.UserLanguageChangeFeatureDependencies
import uz.uzkassa.smartpos.feature.user.settings.password.dependencies.UserPasswordChangeFeatureDependencies
import uz.uzkassa.smartpos.feature.user.settings.phonenumber.dependencies.UserPhoneNumberChangeFeatureDependencies
import uz.uzkassa.smartpos.feature.users.setup.dependencies.UsersSetupFeatureDependencies

interface GlobalFeatureDependencies :
    AccountAuthFeatureDependencies,
    AccountRecoveryPasswordFeatureDependencies,
    AccountRegistrationFeatureDependencies,
    ActivityTypeSelectionFeatureDependencies,
    CashierApayFeatureDependencies,
    PaymentAmountFeatureDependencies,
    BranchDeleteFeatureDependencies,
    BranchListFeatureDependencies,
    BranchSavingFeatureDependencies,
    BranchSelectionSetupFeatureDependencies,
    BrowserFeatureDependecies,
    CategoryListFeatureDependencies,
    CategorySavingFeatureDependencies,
    CategorySelectionFeatureDependencies,
    CategorySetupFeatureDependencies,
    CategoryTypeFeatureDependencies,
    CashierCashOperationsFeatureDependencies,
    CashierRefundFeatureDependencies,
    CashierSaleFeatureDependencies,
    CompanySavingFeatureDependencies,
    CompanyVATSelectionFeatureDependencies,
    PaymentDiscountFeatureDependencies,
    LauncherFeatureDependencies,
    MainCategoriesFeatureDependencies,
    ProductMarkingFeatureDependencies,
    ProductListFeatureDependencies,
    ProductSavingFeatureDependencies,
    ProductUnitCreationFeatureDependencies,
    ProductQuantityFeatureDependencies,
    RegionCitySelectionFeatureDependencies,
    ReceiptCheckFeatureDependencies,
    ReceiptRemoteFeatureDependencies,
    SupervisorDashboardFeatureDependencies,
    UserAuthFeatureDependencies,
    UserConfirmationFeatureDependencies,
    UserLanguageChangeFeatureDependencies,
    UserPasswordChangeFeatureDependencies,
    UserPhoneNumberChangeFeatureDependencies,
    UserListFeatureDependencies,
    UserDataChangeFeatureDependencies,
    UserSavingFeatureDependencies,
    UsersSetupFeatureDependencies,
    UserSettingsFeatureDependencies,
    AutoPrintFeatureDependencies