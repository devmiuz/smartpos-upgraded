package uz.uzkassa.smartpos.feature.launcher.domain.user.auth

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.ReceiptRelation
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.launcher.data.model.user.UsersAuth
import uz.uzkassa.smartpos.feature.launcher.data.repository.branch.CurrentBranchRepository
import uz.uzkassa.smartpos.feature.launcher.data.repository.company.CurrentCompanyRepository
import uz.uzkassa.smartpos.feature.launcher.data.repository.users.UsersRepository
import javax.inject.Inject

internal class UsersAuthInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val currentBranchRepository: CurrentBranchRepository,
    private val currentCompanyRepository: CurrentCompanyRepository,
    private val usersRepository: UsersRepository
) {

    @FlowPreview
    fun getUserAuth(): Flow<Result<UsersAuth>> {
        return usersRepository.getUsersInCurrentBranch()
            .flatMapMerge { users ->
                currentBranchRepository.getCurrentBranch()
                    .flatMapMerge { branch ->
                        currentCompanyRepository.getCurrentCompany()
                            .map {
                                UsersAuth(
                                    company = it,
                                    branch = branch,
                                    ownerLanguage = getOwnerLanguage(users),
                                    cashiers = getCashiers(users),
                                    users = getUsersExceptCashiers(users)
                                )
                            }
                    }
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun getUndeliveredReceipts(): Flow<Result<List<ReceiptRelation>>> {
        return usersRepository
            .getUndeliveredReceipts()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    private fun getCashiers(users: List<User>): List<User> =
        users.filter { !it.isBlocked }
            .filter { it.userRole.type == UserRole.Type.CASHIER || it.userRole.type == UserRole.Type.OWNER }
            .sortedBy { it.userRole.type != UserRole.Type.CASHIER }

    private fun getOwnerLanguage(users: List<User>): Language =
        users.find { it.userRole.type == UserRole.Type.OWNER }?.language ?: Language.DEFAULT

    private fun getUsersExceptCashiers(users: List<User>): List<User> =
        users.filter { !it.isBlocked }
            .filterNot { it.userRole.type == UserRole.Type.CASHIER }

    fun clearAppDataAndLogout(): Flow<Result<Unit>> =
        usersRepository
            .clearAppDataAndLogout()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

    fun syncUndeliveredReceipts(receiptRelations: List<ReceiptRelation>): Flow<Result<Unit>> =
        usersRepository
            .syncUndeliveredReceipts(receiptRelations)
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

}