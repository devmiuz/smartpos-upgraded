package uz.uzkassa.smartpos.feature.helper.payment.amount.dependencies

import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.Amount

interface PaymentAmountFeatureCallback {

    fun onFinish(amount: Amount)
}