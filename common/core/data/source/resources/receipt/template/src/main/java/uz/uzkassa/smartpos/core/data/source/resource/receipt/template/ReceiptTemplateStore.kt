package uz.uzkassa.smartpos.core.data.source.resource.receipt.template

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplate
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.service.ReceiptTemplateRestService
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.core.data.source.resource.store.Store
import uz.uzkassa.smartpos.core.data.source.resource.store.fetcher.Fetcher
import uz.uzkassa.smartpos.core.data.source.resource.store.source.SourceOfTruth
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplateResponse as Response

class ReceiptTemplateStore(
    private val receiptTemplateEntityDao: ReceiptTemplateEntityDao,
    private val receiptTemplateRestService: ReceiptTemplateRestService
) {

    fun getReceiptTemplate(): Store<Unit, ReceiptTemplate?> {
        return Store.from<Unit, Response?, ReceiptTemplate?>(
            fetcher = Fetcher.ofFlow {
                val flow: Flow<Response?> = receiptTemplateRestService.getReceiptTemplate()
                return@ofFlow flow.catch {
                    if (it is NotFoundHttpException) {
                        receiptTemplateEntityDao.deleteAll()
                        emit(null)
                    } else throw it
                }
            },
            sourceOfTruth = SourceOfTruth.of(
                nonFlowReader = { receiptTemplateEntityDao.getEntity()?.map() },
                writer = { _, it -> if (it != null) receiptTemplateEntityDao.save(it) }
            )
        )
    }
}