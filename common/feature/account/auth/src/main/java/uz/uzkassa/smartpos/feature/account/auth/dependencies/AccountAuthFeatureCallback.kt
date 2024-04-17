package uz.uzkassa.smartpos.feature.account.auth.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.account.model.Account

interface AccountAuthFeatureCallback {

    fun onBackFromAccountAuth()

    fun onOpenAccountRecoveryPassword(phoneNumber: String)

    fun onFinishAccountAuth(account: Account)
}