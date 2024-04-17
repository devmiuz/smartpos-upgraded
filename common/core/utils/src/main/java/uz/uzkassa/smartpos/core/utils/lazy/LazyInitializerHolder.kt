package uz.uzkassa.smartpos.core.utils.lazy

abstract class LazyInitializerHolder<V : Any> {

    @Volatile
    private var value: V? = null

    fun initialize(vararg args: Any): V {
        if (value != null) return checkNotNull(value)
        synchronized(this::class.java) {
            if (value == null)
                value = getValue(args.toList().toTypedArray())
        }
        return checkNotNull(value)
    }

    fun notNull(): Boolean = value != null

    fun get(): V = value
        ?: throw RuntimeException("You must call 'initialize' method first from ${this.javaClass.name}")

    open fun reset() {
        value = null
    }

    protected abstract fun getValue(args: Array<Any>): V
}