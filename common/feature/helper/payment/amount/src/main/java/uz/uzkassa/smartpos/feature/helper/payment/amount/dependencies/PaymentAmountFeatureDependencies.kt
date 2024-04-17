package uz.uzkassa.smartpos.feature.helper.payment.amount.dependencies

interface PaymentAmountFeatureDependencies {

    val paymentAmountFeatureArgs: PaymentAmountFeatureArgs

    val paymentAmountFeatureCallback: PaymentAmountFeatureCallback
}