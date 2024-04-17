package uz.uzkassa.smartpos.core.manager.logger

abstract class LoggerTree(internal val isEnabled: Boolean = true) {

    abstract fun v(tag: String, msg: String?)

    abstract fun v(tag: String, msg: String?, tr: Throwable?)

    abstract fun d(tag: String, msg: String?)

    abstract fun d(tag: String, msg: String?, tr: Throwable?)

    abstract fun i(tag: String, msg: String?)

    abstract fun i(tag: String, msg: String?, tr: Throwable?)

    abstract fun w(tag: String, msg: String?)

    abstract fun w(tag: String, msg: String?, tr: Throwable?)

    abstract fun w(tag: String, tr: Throwable?)

    abstract fun e(tag: String, msg: String?)

    abstract fun e(tag: String, msg: String?, tr: Throwable?)

    abstract fun wtf(tag: String, msg: String?)

    abstract fun wtf(tag: String, tr: Throwable?)

    abstract fun wtf(tag: String, msg: String?, tr: Throwable?)
}