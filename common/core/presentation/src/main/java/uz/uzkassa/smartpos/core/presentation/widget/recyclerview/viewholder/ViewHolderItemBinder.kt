package uz.uzkassa.smartpos.core.presentation.widget.recyclerview.viewholder

interface ViewHolderItemBinder<T> {

    fun onBind(element: T)

    fun onUnbind() {
    }
}