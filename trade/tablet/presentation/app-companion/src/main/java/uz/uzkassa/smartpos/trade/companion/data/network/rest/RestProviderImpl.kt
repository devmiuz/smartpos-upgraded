package uz.uzkassa.smartpos.trade.companion.data.network.rest

import retrofit2.Retrofit
import uz.uzkassa.smartpos.core.data.source.resource.account.service.AccountRestService
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.service.ActivityTypeRestService
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.preference.AccountAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.auth.account.service.AccountAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.preference.UserAuthPreference
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.data.source.resource.card.service.CardRestService
import uz.uzkassa.smartpos.core.data.source.resource.category.preference.CategorySyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.data.source.resource.city.service.CityRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.service.CompanyBusinessTypeRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.service.CompanyRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.service.CompanyVATRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.service.ProductPackageTypeRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.preference.ProductSyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.service.ReceiptTemplateRestService
import uz.uzkassa.smartpos.core.data.source.resource.region.service.RegionRestService
import uz.uzkassa.smartpos.core.data.source.resource.shift.report.service.ShiftReportRestService
import uz.uzkassa.smartpos.core.data.source.resource.terminal.service.TerminalRestService
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.role.service.UserRoleRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.trade.companion.data.network.rest.impl.retrofit.RetrofitInstance
import uz.uzkassa.smartpos.trade.companion.data.network.utils.credential.CurrentCredentialParams
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.OkHttpInstance

class RestProviderImpl(
    accountAuthPreference: AccountAuthPreference,
    categorySyncTimePreference: CategorySyncTimePreference,
    currentCredentialParams: CurrentCredentialParams,
    productSyncTimePreference: ProductSyncTimePreference,
    okHttpInstance: OkHttpInstance,
    userAuthPreference: UserAuthPreference
) : RestProvider {
    private val retrofit: Retrofit by lazy {
        RetrofitInstance(currentCredentialParams, okHttpInstance).retrofit
    }

    override val accountRestService by lazy {
        AccountRestService.instantiate(retrofit)
    }

    override val accountAuthRestService by lazy {
        AccountAuthRestService.instantiate(accountAuthPreference, retrofit)
    }

    override val activityTypeRestService by lazy {
        ActivityTypeRestService.instantiate(retrofit)
    }

    override val branchRestService by lazy {
        BranchRestService.instantiate(retrofit)
    }

    override val cardRestService by lazy {
        CardRestService.instantiate(retrofit)
    }

    override val categoryRestService by lazy {
        CategoryRestService.instantiate(retrofit, categorySyncTimePreference)
    }

    override val cityRestService by lazy {
        CityRestService.instantiate(retrofit)
    }

    override val companyRestService by lazy {
        CompanyRestService.instantiate(retrofit)
    }

    override val companyBusinessTypeRestService by lazy {
        CompanyBusinessTypeRestService.instantiate(retrofit)
    }

    override val companyVATRestService by lazy {
        CompanyVATRestService.instantiate(retrofit)
    }

    override val productRestService by lazy {
        ProductRestService.instantiate(retrofit, productSyncTimePreference)
    }

    override val productPackageTypeRestService by lazy {
        ProductPackageTypeRestService.instantiate(retrofit)
    }

    override val regionRestService by lazy {
        RegionRestService.instantiate(retrofit)
    }

    override val receiptRestService by lazy {
        ReceiptRestService.instantiate(retrofit)
    }

    override val receiptTemplateRestService by lazy {
        ReceiptTemplateRestService.instantiate(retrofit)
    }

    override val shiftReportRestService by lazy {
        ShiftReportRestService.instantiate(retrofit)
    }

    override val terminalRestService by lazy {
        TerminalRestService.instantiate(retrofit)
    }

    override val unitRestService by lazy {
        UnitRestService.instantiate(retrofit)
    }

    override val userAuthRestService by lazy {
        UserAuthRestService.instantiate(userAuthPreference, retrofit)
    }

    override val userRestService by lazy {
        UserRestService.instantiate(retrofit)
    }

    override val userRoleRestService by lazy {
        UserRoleRestService.instantiate(retrofit)
    }
}