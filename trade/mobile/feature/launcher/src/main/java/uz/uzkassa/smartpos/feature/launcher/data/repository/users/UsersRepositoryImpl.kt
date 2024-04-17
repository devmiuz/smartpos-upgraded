package uz.uzkassa.smartpos.feature.launcher.data.repository.users

import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.preference.cleaner.PreferenceCleaner
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.mapToResponses
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptRelation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.user.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import uz.uzkassa.smartpos.core.utils.kserialization.json.actual
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import javax.inject.Inject

internal class UsersRepositoryImpl @Inject constructor(
    private val currentBranchPreference: CurrentBranchPreference,
    private val userRelationDao: UserRelationDao,
    private val preferenceCleaner: PreferenceCleaner,
    private val receiptRelationDao: ReceiptRelationDao,
    private val receiptRestService: ReceiptRestService,
    private val receiptEntityDao: ReceiptEntityDao
) : UsersRepository {

    @FlowPreview
    override fun getUsersInCurrentBranch(): Flow<List<User>> {
        return flowOf(currentBranchPreference.branchId)
            .filterNotNull()
            .flatMapConcat {
                userRelationDao.getRelationsFlowByBranchId(it)
            }
            .map { it.map() }
    }

    override fun clearAppDataAndLogout(): Flow<Unit> {
        return flowOf(Unit)
            .onEach {
                userRelationDao.clearAllTables()
                preferenceCleaner.clearAll()
            }
    }

    @UnstableDefault
    @FlowPreview
    override fun getUndeliveredReceipts(): Flow<List<ReceiptRelation>> {
        return flow {
            emit(receiptRelationDao.getRelationsBySync(false))
        }
    }

    @UnstableDefault
    override fun syncUndeliveredReceipts(relations: List<ReceiptRelation>): Flow<Unit> {
        if (relations.isEmpty()) return flowOf(Unit)

        val jsonElement: JsonElement = Json.actual.toJson(
            serializer = ListSerializer(ReceiptResponse.serializer()),
            value = relations.mapToResponses()
        )

        return receiptRestService.createReceiptByBatch(jsonElement)
            .onEach {
                receiptEntityDao.updateSyncState(
                    relations.map {
                        it.receiptEntity.uid
                    })
            }
            .map { }
    }
}