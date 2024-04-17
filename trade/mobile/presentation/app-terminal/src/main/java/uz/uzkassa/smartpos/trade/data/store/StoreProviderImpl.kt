package uz.uzkassa.smartpos.trade.data.store

import uz.uzkassa.smartpos.core.data.source.resource.branch.BranchStore
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.CompanyStore
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.company.service.CompanyRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.ReceiptTemplateStore
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.service.ReceiptTemplateRestService
import uz.uzkassa.smartpos.core.data.source.resource.unit.UnitStore
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.UserStore
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.service.UserRoleRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class StoreProviderImpl @Inject constructor(
    branchEntityDao: BranchEntityDao,
    branchRelationDao: BranchRelationDao,
    branchRestService: BranchRestService,
    companyEntityDao: CompanyEntityDao,
    companyRelationDao: CompanyRelationDao,
    companyRestService: CompanyRestService,
    receiptTemplateEntityDao: ReceiptTemplateEntityDao,
    receiptTemplateRestService: ReceiptTemplateRestService,
    unitEntityDao: UnitEntityDao,
    unitRestService: UnitRestService,
    userEntityDao: UserEntityDao,
    userRelationDao: UserRelationDao,
    userRestService: UserRestService,
    userRoleEntityDao: UserRoleEntityDao,
    userRoleRestService: UserRoleRestService
) : StoreProvider {

    override val branchStore by lazy(NONE) {
        BranchStore(
            branchEntityDao = branchEntityDao,
            branchRelationDao = branchRelationDao,
            branchRestService = branchRestService
        )
    }

    override val companyStore by lazy(NONE) {
        CompanyStore(
            companyEntityDao = companyEntityDao,
            companyRelationDao = companyRelationDao,
            companyRestService = companyRestService
        )
    }

    override val receiptTemplateStore by lazy(NONE) {
        ReceiptTemplateStore(
            receiptTemplateEntityDao = receiptTemplateEntityDao,
            receiptTemplateRestService = receiptTemplateRestService
        )
    }

    override val unitStore by lazy(NONE) {
        UnitStore(
            unitEntityDao = unitEntityDao,
            unitRestService = unitRestService
        )
    }

    override val userStore by lazy(NONE) {
        UserStore(
            userEntityDao = userEntityDao,
            userRelationDao = userRelationDao,
            userRestService = userRestService,
            userRoleEntityDao = userRoleEntityDao,
            userRoleRestService = userRoleRestService
        )
    }
}