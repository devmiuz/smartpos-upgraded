package uz.uzkassa.smartpos.trade.data.store

import uz.uzkassa.smartpos.core.data.source.resource.branch.BranchStore
import uz.uzkassa.smartpos.core.data.source.resource.company.CompanyStore
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.ReceiptTemplateStore
import uz.uzkassa.smartpos.core.data.source.resource.unit.UnitStore
import uz.uzkassa.smartpos.core.data.source.resource.user.UserStore

interface StoreProvider {

    val branchStore: BranchStore

    val companyStore: CompanyStore

    val receiptTemplateStore: ReceiptTemplateStore

    val unitStore: UnitStore

    val userStore: UserStore
}