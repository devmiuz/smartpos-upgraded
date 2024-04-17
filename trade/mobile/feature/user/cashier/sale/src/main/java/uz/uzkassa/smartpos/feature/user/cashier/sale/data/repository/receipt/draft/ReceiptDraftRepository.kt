package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model.ReceiptDraft
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.params.ReceiptDraftDeleteParams
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.params.ReceiptDraftSaveParams

internal interface ReceiptDraftRepository {

    fun createReceiptDraft(params: ReceiptDraftSaveParams): Flow<Unit>

    fun deleteReceiptDraft(params: ReceiptDraftDeleteParams): Flow<Unit>

    fun getReceiptDraftById(id: Long): Flow<ReceiptDraft>

    fun getReceiptDrafts(search: String?): Flow<List<ReceiptDraft>>
}