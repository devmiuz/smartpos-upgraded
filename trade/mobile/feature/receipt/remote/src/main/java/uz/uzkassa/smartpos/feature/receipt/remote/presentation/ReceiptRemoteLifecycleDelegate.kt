package uz.uzkassa.smartpos.feature.receipt.remote.presentation

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.customlifecycle.StoredComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpDelegate
import moxy.MvpDelegateHolder
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.alert.AlertDialogDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.lifecycle.LifecycleDelegate
import uz.uzkassa.smartpos.feature.receipt.remote.R
import uz.uzkassa.smartpos.feature.receipt.remote.presentation.di.ReceiptRemoteComponent
import javax.inject.Inject

class ReceiptRemoteLifecycleDelegate(
    context: Context,
    lifecycleOwner: LifecycleOwner?
) : LifecycleDelegate(lifecycleOwner), MvpDelegateHolder, ReceiptRemoteView,
    IHasComponent<ReceiptRemoteComponent> {
    private var storedComponent: StoredComponent<ReceiptRemoteComponent>? = null
    private var mvpDelegate: MvpDelegate<ReceiptRemoteLifecycleDelegate>? = null

    @Inject
    internal lateinit var lazyPresenter: Lazy<ReceiptRemotePresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val alertDialogDelegate by lazy { AlertDialogDelegate(context, lifecycleOwner) }

    override fun getComponent(): ReceiptRemoteComponent =
        ReceiptRemoteComponent.create(XInjectionManager.findComponent())

    override fun onCreate() {
        storedComponent =
            XInjectionManager.bindComponentToCustomLifecycle(this)
                .also { it.component.inject(this) }

        getMvpDelegate().onCreate()
    }

    override fun onStart() =
        getMvpDelegate().onAttach()

    override fun onResume() =
        getMvpDelegate().onAttach()

    fun onSaveInstanceState(outState: Bundle?) =
        getMvpDelegate().also { it.onSaveInstanceState(outState) }.onDetach()

    override fun onStop() =
        getMvpDelegate().onDetach()

    override fun onDestroy() {
        getMvpDelegate().onDetach()
        getMvpDelegate().onDestroyView()
        storedComponent?.lifecycle?.destroy()
        storedComponent = null
        super.onDestroy()
    }

    override fun onShowReceivedReceiptRemoteAlert() {
        alertDialogDelegate.apply {
            newBuilder {
                setTitle(R.string.delegate_feature_remote_receipt_received_alert_title)
                setMessage(R.string.delegate_feature_remote_receipt_received_alert_message)
                setPositiveButton(R.string.core_presentation_common_ok) { _, _ -> presenter.dismissReceivedReceiptRemoteAlert() }
                setOnDismissListener { presenter.dismissReceivedReceiptRemoteAlert() }
            }
        }.show()
    }

    override fun onDismissReceivedReceiptRemoteAlert() {
        alertDialogDelegate.dismiss()
    }

    override fun getMvpDelegate(): MvpDelegate<ReceiptRemoteLifecycleDelegate> {
        if (mvpDelegate == null) mvpDelegate = MvpDelegate(this)
        return mvpDelegate as MvpDelegate<ReceiptRemoteLifecycleDelegate>
    }
}