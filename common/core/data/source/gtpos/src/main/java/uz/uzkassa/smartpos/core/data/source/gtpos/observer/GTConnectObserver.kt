package uz.uzkassa.smartpos.core.data.source.gtpos.observer

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import mdd.libs.gtpos.connect.GTConnectOrder
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSErrorType.UNKNOWN
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSException
import uz.uzkassa.smartpos.core.data.source.gtpos.intent.GTPOSLaunchIntentImpl
import uz.uzkassa.smartpos.core.data.source.gtpos.mapper.mapToGTConnectOrder
import uz.uzkassa.smartpos.core.data.source.gtpos.mapper.mapToGTPOSPaymentResult
import uz.uzkassa.smartpos.core.data.source.gtpos.mapper.mapToResult
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Request
import uz.uzkassa.smartpos.core.data.source.gtpos.model.operation.GTPOSOperation.Response
import uz.uzkassa.smartpos.core.data.source.gtpos.model.result.GTConnectResult
import uz.uzkassa.smartpos.core.data.source.gtpos.model.result.GTPOSResult
import java.lang.ref.WeakReference

@Suppress("EXPERIMENTAL_API_USAGE", "unused")
internal class GTConnectObserver(
    private val launchIntent: GTPOSLaunchIntentImpl
) : LifecycleObserver {
    private val broadcastChannel = BroadcastChannel<GTPOSResult<Response>>(1)
    private var activityResultCallback: ActivityResultCallback<GTConnectResult?>? = null
    private var activityResultLauncher: ActivityResultLauncher<GTConnectOrder>? = null
    private var activityResultRegistry: WeakReference<ActivityResultRegistry>? = null
    private var lifecycle: Lifecycle? = null

    fun register(activity: ComponentActivity) {
        activityResultRegistry = WeakReference(activity.activityResultRegistry)
        lifecycle = activity.lifecycle.also { it.addObserver(this) }
    }

    fun register(fragment: Fragment) {
        activityResultRegistry = WeakReference(fragment.requireActivity().activityResultRegistry)
        lifecycle = fragment.lifecycle.also { it.addObserver(this) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Response> request(request: Request): Flow<GTPOSResult<T>> {
        return broadcastChannel
            .asFlow()
            .onStart {
                runCatching { activityResultLauncher?.launch(request.mapToGTConnectOrder()) }
                    .onFailure {
                        val errorType = (it as? GTPOSException)?.gtposErrorType ?: UNKNOWN
                        emit(GTPOSResult.error(errorType))
                    }
            }
            .map { it as GTPOSResult<T> }
            .take(1)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate(owner: LifecycleOwner) {
        activityResultCallback = GTPaymentResultCallback()
        activityResultLauncher = activityResultRegistry?.get()?.register(
            GTPOSLaunchIntentImpl.PACKAGE_NAME,
            owner,
            GTConnectOrderResultContract(),
            checkNotNull(activityResultCallback)
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        lifecycle?.removeObserver(this)
        activityResultRegistry?.clear()
        activityResultCallback = null
        activityResultLauncher = null
        lifecycle = null
    }

    private inner class GTConnectOrderResultContract :
        ActivityResultContract<GTConnectOrder, GTConnectResult?>() {

        override fun createIntent(context: Context, input: GTConnectOrder): Intent {
            return launchIntent
                .intentOrThrow()
                .putExtra("GTConnectOrder", input)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): GTConnectResult? =
            runCatching { GTConnectOrder.getOrder(intent) }.getOrNull()?.mapToGTPOSPaymentResult()
    }

    private inner class GTPaymentResultCallback : ActivityResultCallback<GTConnectResult?> {
        override fun onActivityResult(result: GTConnectResult?) =
            broadcastChannel.sendBlocking(result.mapToResult())
    }
}