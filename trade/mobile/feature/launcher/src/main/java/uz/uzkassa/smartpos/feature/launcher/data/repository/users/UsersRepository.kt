package uz.uzkassa.smartpos.feature.launcher.data.repository.users

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptRelation
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal interface UsersRepository {

    fun getUsersInCurrentBranch(): Flow<List<User>>

    fun clearAppDataAndLogout(): Flow<Unit>

    fun getUndeliveredReceipts(): Flow<List<ReceiptRelation>>

    fun syncUndeliveredReceipts(relations: List<ReceiptRelation>): Flow<Unit>
}