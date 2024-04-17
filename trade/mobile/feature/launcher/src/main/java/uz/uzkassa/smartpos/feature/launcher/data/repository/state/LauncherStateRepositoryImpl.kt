package uz.uzkassa.smartpos.feature.launcher.data.repository.state

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.launcher.data.model.launcher.LauncherState
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import uz.uzkassa.smartpos.feature.launcher.data.preference.state.LauncherStatePreference
import uz.uzkassa.smartpos.feature.launcher.dependencies.LauncherFeatureArgs
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.StartScreenType
import javax.inject.Inject

internal class LauncherStateRepositoryImpl @Inject constructor(
    private val currentBranchPreference: CurrentBranchPreference,
    private val launcherFeatureArgs: LauncherFeatureArgs,
    private val launcherStatePreference: LauncherStatePreference
) : LauncherStateRepository {

    private val startScreenTypeBroadcastChannel: BroadcastChannelWrapper<StartScreenType>
        get() = launcherFeatureArgs.startScreenTypeBroadcastChannel

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun getLauncherState(): Flow<LauncherState> {
        return startScreenTypeBroadcastChannel
            .asFlow()
            .onEach { applyChanges(it) }
            .flatMapMerge { getCurrentLauncherState(it) }
    }

    override fun setLauncherStateByStartScreenType(startScreenType: StartScreenType) =
        startScreenTypeBroadcastChannel.sendBlocking(startScreenType)

    private fun applyChanges(startScreenType: StartScreenType) {
        when (startScreenType) {
            StartScreenType.AUTO -> Unit

            StartScreenType.ACCOUNT_LOGIN_COMPLETED ->
                launcherStatePreference.apply {
                    isAccountNotAuthenticated = false
                    isAccountLoginNotCompleted = true
                }

            StartScreenType.COMPANY_CREATION_COMPLETED ->
                launcherStatePreference.isCompanyNotDefined = false

            is StartScreenType.CURRENT_BRANCH_SELECTION_COMPLETED -> {
                currentBranchPreference.branchId = startScreenType.branchId
                launcherStatePreference.isCurrentBranchNotDefined = false
            }

            StartScreenType.USER_CREATION_COMPLETED ->
                launcherStatePreference.isUsersNotDefined = false

            StartScreenType.CATEGORY_SETUP_COMPLETED ->
                launcherStatePreference.isCategoriesNotDefined = false
        }
    }

    private fun getCurrentLauncherState(startScreenType: StartScreenType): Flow<LauncherState> {
        val isCompanyDetailsDefined: Boolean = launcherStatePreference.let {
            !it.isCompanyNotDefined || !it.isCurrentBranchNotDefined ||
                    !it.isUsersNotDefined || !it.isCategoriesNotDefined
        }

        val isNotDefined: Boolean =
            launcherStatePreference.isAccountLoginNotCompleted || isCompanyDetailsDefined

        val state: LauncherState = when {
            launcherStatePreference.isLanguageNotDefined ->
                LauncherState.LANGUAGE_SELECTION
            launcherStatePreference.isAccountNotAuthenticated ->
                LauncherState.ACCOUNT_AUTH_SELECTION
            isNotDefined ->
                if (startScreenType != StartScreenType.AUTO)
                    when (startScreenType) {
                        StartScreenType.COMPANY_CREATION_COMPLETED -> LauncherState.USER_CREATION
                        is StartScreenType.CURRENT_BRANCH_SELECTION_COMPLETED -> LauncherState.SYNC
                        StartScreenType.USER_CREATION_COMPLETED -> {
                            if (launcherStatePreference.isCategoriesNotDefined)
                                LauncherState.CATEGORY_SETUP
                            else LauncherState.USER_AUTH
                        }
                        else -> LauncherState.USER_AUTH
                    }
                else LauncherState.ACCOUNT_LOGIN_COMPLETE
            else -> LauncherState.USER_AUTH
        }

        return flow { emit(state) }
    }
}