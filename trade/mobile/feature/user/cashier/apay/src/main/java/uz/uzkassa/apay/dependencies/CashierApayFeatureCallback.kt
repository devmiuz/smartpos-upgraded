package uz.uzkassa.apay.dependencies

import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.amount.Amount

interface CashierApayFeatureCallback {

    fun back()

    fun finish(paymentAmount: Amount, billId :String)

}