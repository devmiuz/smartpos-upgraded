package uz.uzkassa.smartpos.feature.helper.payment.discount.dependencies

import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.Discount

interface PaymentDiscountFeatureCallback {

    fun onFinish(discount: Discount)

    fun onBackFromPaymentDiscount()
}