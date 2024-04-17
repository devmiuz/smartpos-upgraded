package uz.uzkassa.smartpos.feature.helper.payment.discount.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.card.dao.CardEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.card.dao.CardTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.card.service.CardRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface PaymentDiscountFeatureDependencies {

    val cardEntityDao: CardEntityDao

    val cardTypeEntityDao: CardTypeEntityDao

    val coroutineContextManager: CoroutineContextManager

    val cardRestService: CardRestService

    val paymentDiscountFeatureArgs: PaymentDiscountFeatureArgs

    val paymentDiscountFeatureCallback: PaymentDiscountFeatureCallback
}