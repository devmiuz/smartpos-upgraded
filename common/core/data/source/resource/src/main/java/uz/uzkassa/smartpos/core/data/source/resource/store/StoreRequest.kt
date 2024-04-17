package uz.uzkassa.smartpos.core.data.source.resource.store;

data class StoreRequest<Key>(val key: Key, val refresh: Boolean = false)