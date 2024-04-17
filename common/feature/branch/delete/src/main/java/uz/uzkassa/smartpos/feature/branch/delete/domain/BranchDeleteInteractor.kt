package uz.uzkassa.smartpos.feature.branch.delete.domain

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.HttpException
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.branch.delete.data.exception.BranchDeleteException
import uz.uzkassa.smartpos.feature.branch.delete.data.exception.WrongConfirmationCodeException
import uz.uzkassa.smartpos.feature.branch.delete.data.repository.BranchRepository
import uz.uzkassa.smartpos.feature.branch.delete.data.repository.delete.BranchDeletionRepository
import uz.uzkassa.smartpos.feature.branch.delete.data.repository.delete.params.FinishDeleteParams
import uz.uzkassa.smartpos.feature.branch.delete.data.repository.delete.params.RequestDeleteParams
import javax.inject.Inject
import kotlin.properties.Delegates

internal class BranchDeleteInteractor @Inject constructor(
    private val branchDeletionRepository: BranchDeletionRepository,
    private val branchRepository: BranchRepository,
    private val coroutineContextManager: CoroutineContextManager
) {
    private var branch: Branch by Delegates.notNull()
    private var code: Long? = null
    private var deleteReason: String? = null

    @FlowPreview
    fun deleteBranch(): Flow<Result<Long>> =
        branchRepository
            .getBranch()
            .onEach { branch = it }
            .flatMapConcat { branch ->
                branchDeletionRepository
                    .deleteBranch(RequestDeleteParams(branch.id))
                    .map { branch.id }
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

    @Deprecated("")
    fun setConfirmationCode(value: String) {
        code = if (value.isNotEmpty()) TextUtils.replaceAllLetters(value).toLong() else null
    }

    @Deprecated("")
    fun setReason(value: String) {
        deleteReason = value
    }

    @Deprecated("")
    fun finishDeleteBranch(): Flow<Result<Long>> {
        val branchDeleteException =
            BranchDeleteException(
                confirmationCodeNotDefined = code == null || code?.toString()?.length?.compareTo(5) == 0,
                reasonNotDefined = deleteReason.isNullOrBlank()
            )

        return when {
            branchDeleteException.isPassed ->
                flowOf(Result.failure(branchDeleteException))
            else ->
                branchDeletionRepository
                    .finishDelete(
                        FinishDeleteParams(
                            id = branch.id,
                            name = branch.name,
                            confirmationCode = checkNotNull(code),
                            deleteReason = checkNotNull(deleteReason)
                        )
                    )
                    .catch {
                        throw if (it is HttpException && it.response.httpErrorCode == 500)
                            WrongConfirmationCodeException()
                        else it
                    }
                    .map { branch.id }
                    .flatMapResult()
                    .flowOn(coroutineContextManager.ioContext)
        }
    }
}