package uz.uzkassa.smartpos.core.presentation.support.delegate.view.recyclerview.state

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import uz.uzkassa.smartpos.core.presentation.R
import uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.state.RecyclerViewState

abstract class RecyclerViewStateEvent : RecyclerViewState {

    open var isContent: Boolean = false
        internal set

    open class EmptyRecyclerViewStateEvent : RecyclerViewStateEvent() {
        override fun onCreateView(inflater: LayoutInflater, root: ViewGroup): View {
            val resId: Int = R.layout.view_holder_recycler_view_state_empty
            val view: View = inflater.inflate(resId, root, false)
            view.layoutParams = view.layoutParams.apply {
                height = if (isContent) ViewGroup.LayoutParams.MATCH_PARENT
                else ViewGroup.LayoutParams.WRAP_CONTENT
            }
            return view
        }
    }

    open class ErrorRecyclerViewStateEvent : RecyclerViewStateEvent() {
        private var onRetryClickListener: (() -> Unit)? = null
        private var throwable: Throwable? = null

        private lateinit var retryButton: MaterialButton

        override fun onCreateView(inflater: LayoutInflater, root: ViewGroup): View {
            val resId: Int = R.layout.view_holder_state_recycler_view_state_error
            val view: View = inflater.inflate(resId, root, false)
            view.layoutParams = view.layoutParams.apply {
                height = if (isContent) ViewGroup.LayoutParams.MATCH_PARENT
                else ViewGroup.LayoutParams.WRAP_CONTENT
            }
            retryButton = view.findViewById(android.R.id.button1)
            return view
        }

        fun setError(
            throwable: Throwable,
            onRetryClickListener: (() -> Unit)? = null
        ) {
            this.throwable = throwable
            this.onRetryClickListener = onRetryClickListener
        }

        override fun onBind(viewHolder: RecyclerView.ViewHolder) {
            retryButton.apply {
                if (onRetryClickListener == null) visibility = View.GONE
                else setOnClickListener { onRetryClickListener?.invoke() }
            }
        }

        override fun onUnbind(viewHolder: RecyclerView.ViewHolder) {
            onRetryClickListener = null
        }
    }

    open class LoadingRecyclerViewStateEvent : RecyclerViewStateEvent() {
        override fun onCreateView(inflater: LayoutInflater, root: ViewGroup): View {
            val resId: Int = R.layout.view_holder_recycler_view_state_loading
            val view: View = inflater.inflate(resId, root, false)
            view.layoutParams = view.layoutParams.apply {
                height = if (isContent) ViewGroup.LayoutParams.MATCH_PARENT
                else ViewGroup.LayoutParams.WRAP_CONTENT
            }
            return view
        }
    }
}