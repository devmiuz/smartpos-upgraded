package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.state

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface RecyclerViewState {

    fun onCreateView(inflater: LayoutInflater, root: ViewGroup): View

    fun onBind(viewHolder: RecyclerView.ViewHolder) {
    }

    fun onUnbind(viewHolder: RecyclerView.ViewHolder) {
    }
}