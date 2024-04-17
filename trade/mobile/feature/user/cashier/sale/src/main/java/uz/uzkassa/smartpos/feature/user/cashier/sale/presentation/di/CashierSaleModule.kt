package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout.DrawerStateDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.draft.ReceiptDraftProductsBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.held.ReceiptHeldBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.marking.ProductMarkingRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.marking.ProductMarkingRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.credit_advance.CreditAdvanceRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.credit_advance.CreditAdvanceRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.ReceiptDraftRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.ReceiptDraftRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save.ReceiptSaleSaveRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save.ReceiptSaleSaveRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.shift.ShiftReportRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.shift.ShiftReportRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user.UserRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user.UserRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user.session.UserSessionRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user.session.UserSessionRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.rest.ApayRestService
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.SaleInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.marking.ProductMarkingInteractor

@Module(
    includes = [
        CashierSaleModule.Binders::class,
        CashierSaleModule.Providers::class
    ]
)
internal object CashierSaleModule {

    @Module
    interface Binders {

        @Binds
        @CashierSaleScope
        fun bindReceiptDraftRepository(
            impl: ReceiptDraftRepositoryImpl
        ): ReceiptDraftRepository

        @Binds
        @CashierSaleScope
        fun bindReceiptSaveRepository(
            impl: ReceiptSaleSaveRepositoryImpl
        ): ReceiptSaleSaveRepository

        @Binds
        @CashierSaleScope
        fun bindShiftReportRepository(
            shiftReportRepositoryImpl: ShiftReportRepositoryImpl
        ): ShiftReportRepository

        @Binds
        @CashierSaleScope
        fun bindUserRepository(
            userRepositoryImpl: UserRepositoryImpl
        ): UserRepository

        @Binds
        @CashierSaleScope
        fun bindUserSessionRepository(
            userSessionRepositoryImpl: UserSessionRepositoryImpl
        ): UserSessionRepository

        @Binds
        @CashierSaleScope
        fun bindProductMarkingRepository(
            impl: ProductMarkingRepositoryImpl
        ): ProductMarkingRepository

        @Binds
        @CashierSaleScope
        fun bindCreditAdvanceRepository(
            impl: CreditAdvanceRepositoryImpl
        ): CreditAdvanceRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @CashierSaleScope
        fun provideDrawerStateDelegate(): DrawerStateDelegate =
            DrawerStateDelegate()

        @JvmStatic
        @Provides
        @CashierSaleScope
        fun provideReceiptDraftProductsBroadcastChannel(): ReceiptDraftProductsBroadcastChannel =
            ReceiptDraftProductsBroadcastChannel()

        @JvmStatic
        @Provides
        @CashierSaleScope
        fun provideReceiptHeldBroadcastChannel(): ReceiptHeldBroadcastChannel =
            ReceiptHeldBroadcastChannel()

        @JvmStatic
        @Provides
        @CashierSaleScope
        fun provideSaleInteractor(): SaleInteractor =
            SaleInteractor()

        @JvmStatic
        @Provides
        @CashierSaleScope
        fun provideProductMarkingInteractor(
            coroutineContextManager: CoroutineContextManager,
            productMarkingRepository: ProductMarkingRepository
        ): ProductMarkingInteractor =
            ProductMarkingInteractor(
                coroutineContextManager,
                productMarkingRepository
            )

        @JvmStatic
        @Provides
        @CashierSaleScope
        fun provideApayRestService(retrofit: Retrofit): ApayRestService =
            retrofit.create()
    }
}