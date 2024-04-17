package uz.uzkassa.smartpos.trade.companion.data.network.rest

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
import uz.uzkassa.smartpos.trade.companion.data.network.utils.credential.CurrentCredentialParams
import uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.OkHttpInstance

interface RestProvider {

    val accountRestService: AccountRestService

    val accountAuthRestService: AccountAuthRestService

    val activityTypeRestService: ActivityTypeRestService

    val branchRestService: BranchRestService

    val cardRestService: CardRestService

    val categoryRestService: CategoryRestService

    val cityRestService: CityRestService

    val companyRestService: CompanyRestService

    val companyBusinessTypeRestService: CompanyBusinessTypeRestService

    val companyVATRestService: CompanyVATRestService

    val productRestService: ProductRestService

    val productPackageTypeRestService: ProductPackageTypeRestService

    val regionRestService: RegionRestService

    val receiptRestService: ReceiptRestService

    val receiptTemplateRestService: ReceiptTemplateRestService

    val shiftReportRestService: ShiftReportRestService

    val terminalRestService: TerminalRestService

    val unitRestService: UnitRestService

    val userAuthRestService: UserAuthRestService

    val userRestService: UserRestService

    val userRoleRestService: UserRoleRestService

    companion object {

        fun instantiate(
            accountAuthPreference: AccountAuthPreference,
            categorySyncTimePreference: CategorySyncTimePreference,
            currentCredentialParams: CurrentCredentialParams,
            productSyncTimePreference: ProductSyncTimePreference,
            okHttpInstance: OkHttpInstance,
            userAuthPreference: UserAuthPreference
        ): RestProvider =
            RestProviderImpl(
                accountAuthPreference = accountAuthPreference,
                categorySyncTimePreference = categorySyncTimePreference,
                currentCredentialParams = currentCredentialParams,
                productSyncTimePreference = productSyncTimePreference,
                okHttpInstance = okHttpInstance,
                userAuthPreference = userAuthPreference
            )
    }
}