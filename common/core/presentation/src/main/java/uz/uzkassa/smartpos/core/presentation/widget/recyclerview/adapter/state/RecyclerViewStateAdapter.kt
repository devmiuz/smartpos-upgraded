package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.adapter.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

@Suppress("MemberVisibilityCanBePrivate", "unused")
class RecyclerViewStateAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val statesMap: MutableMap<RecyclerViewState, Boolean> = hashMapOf()

    constructor(vararg states: RecyclerViewState) : this() {
        statesMap.putAll(states.map { it to false })
    }

    init {
        statesMap[UNDEFINED] = true
    }

    @Volatile
    private var inflater: LayoutInflater? = null

    @Suppress("MemberVisibilityCanBePrivate")
    override fun getItemViewType(position: Int): Int =
        getCurrentState().hashCode()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        StateViewHolder(getCurrentState(), getLayoutInflater(parent.context), parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as StateViewHolder).onBind()

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) =
        (holder as StateViewHolder).onUnbind()

    override fun getItemCount(): Int =
        if (getCurrentState() == UNDEFINED) 0 else 1

    fun getCurrentState(): RecyclerViewState =
        statesMap.entries.find { it.value }?.key ?: UNDEFINED

    @Suppress("UNCHECKED_CAST")
    fun <T : RecyclerViewState> findState(kClass: KClass<out T>): T =
        statesMap.keys.find { it::class == kClass } as T

    fun registerState(state: RecyclerViewState) {
        statesMap[state] = false
    }

    @Suppress("SameParameterValue")
    fun setState(kClass: KClass<out RecyclerViewState>, notify: Boolean = true) {
        val state: RecyclerViewState =
            statesMap.keys.find { it::class == kClass }
                ?: throw RuntimeException("Unable to find state by class ${kClass.simpleName}")

        for (entry: MutableMap.MutableEntry<RecyclerViewState, Boolean> in statesMap)
            entry.setValue(entry.key == state)

        if (notify) notifyDataSetChanged()
    }

    fun clearState() =
        setState(UNDEFINED::class)

    private fun getLayoutInflater(context: Context) =
        if (inflater != null) checkNotNull(inflater)
        else synchronized(this) {
            if (inflater != null) return checkNotNull(inflater)
            inflater = LayoutInflater.from(context)
            return checkNotNull(inflater)
        }

    private class StateViewHolder(
        private val state: RecyclerViewState,
        inflater: LayoutInflater,
        root: ViewGroup
    ) : RecyclerView.ViewHolder(state.onCreateView(inflater, root)) {

        fun onBind() =
            state.onBind(this)

        fun onUnbind() =
            state.onUnbind(this)
    }

    private object UNDEFINED : RecyclerViewState {

        override fun onCreateView(inflater: LayoutInflater, root: ViewGroup): View =
            throw UnsupportedOperationException()

        override fun onBind(viewHolder: RecyclerView.ViewHolder) =
            throw UnsupportedOperationException()

        override fun onUnbind(viewHolder: RecyclerView.ViewHolder) =
            throw UnsupportedOperationException()
    }
}