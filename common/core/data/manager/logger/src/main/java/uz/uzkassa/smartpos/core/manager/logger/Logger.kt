package uz.uzkassa.smartpos.core.manager.logger

import uz.uzkassa.smartpos.core.manager.logger.impl.AndroidLoggerTreeImpl

class Logger internal constructor(
    vararg tree: LoggerTree
) : LoggerTree() {
    private val trees: List<LoggerTree> = tree.filter { it.isEnabled }

    override fun v(tag: String, msg: String?) =
        trees.forEach { it.v(tag, msg) }

    override fun v(tag: String, msg: String?, tr: Throwable?) =
        trees.forEach { it.v(tag, msg, tr) }

    override fun d(tag: String, msg: String?) =
        trees.forEach { it.d(tag, msg) }

    override fun d(tag: String, msg: String?, tr: Throwable?) =
        trees.forEach { it.d(tag, msg, tr) }

    override fun i(tag: String, msg: String?) =
        trees.forEach { it.i(tag, msg) }

    override fun i(tag: String, msg: String?, tr: Throwable?) =
        trees.forEach { it.i(tag, msg, tr) }

    override fun w(tag: String, msg: String?) =
        trees.forEach { it.w(tag, msg) }

    override fun w(tag: String, msg: String?, tr: Throwable?) =
        trees.forEach { it.w(tag, msg) }

    override fun w(tag: String, tr: Throwable?) =
        trees.forEach { it.w(tag, tr) }

    override fun e(tag: String, msg: String?) =
        trees.forEach { it.e(tag, msg) }

    override fun e(tag: String, msg: String?, tr: Throwable?) =
        trees.forEach { it.e(tag, msg, tr) }

    override fun wtf(tag: String, msg: String?) =
        trees.forEach { it.wtf(tag, msg) }

    override fun wtf(tag: String, tr: Throwable?) =
        trees.forEach { it.wtf(tag, tr) }

    override fun wtf(tag: String, msg: String?, tr: Throwable?) =
        trees.forEach { it.wtf(tag, msg, tr) }

    @Suppress("unused")
    companion object : LoggerTree() {
        @Volatile
        private var logger: Logger? = null

        fun init(vararg tree: LoggerTree) {
            if (logger != null) throw IllegalStateException("Logger already initialized")
            val defaultList: Array<LoggerTree> =
                tree.toMutableList()
                    .apply { add(AndroidLoggerTreeImpl) }
                    .toTypedArray()

            logger = Logger(*defaultList)

            Thread.setDefaultUncaughtExceptionHandler { _, e ->
                logger?.wtf("!!! UNKNOWN ERROR !!!", e)
                Thread.setDefaultUncaughtExceptionHandler(null)
                throw e
            }
        }

        override fun v(tag: String, msg: String?) {
            logger?.v(tag, msg)
        }

        override fun v(tag: String, msg: String?, tr: Throwable?) {
            logger?.v(tag, msg, tr)
        }

        override fun d(tag: String, msg: String?) {
            logger?.d(tag, msg)
        }

        override fun d(tag: String, msg: String?, tr: Throwable?) {
            logger?.d(tag, msg, tr)
        }

        override fun i(tag: String, msg: String?) {
            logger?.i(tag, msg)
        }

        override fun i(tag: String, msg: String?, tr: Throwable?) {
            logger?.i(tag, msg, tr)
        }

        override fun w(tag: String, msg: String?) {
            logger?.w(tag, msg)
        }

        override fun w(tag: String, msg: String?, tr: Throwable?) {
            logger?.w(tag, msg, tr)
        }

        override fun w(tag: String, tr: Throwable?) {
            logger?.w(tag, tr)
        }

        override fun e(tag: String, msg: String?) {
            logger?.e(tag, msg)
        }

        override fun e(tag: String, msg: String?, tr: Throwable?) {
            logger?.e(tag, msg, tr)
        }

        override fun wtf(tag: String, msg: String?) {
            logger?.wtf(tag, msg)
        }

        override fun wtf(tag: String, tr: Throwable?) {
            logger?.wtf(tag, tr)
        }

        override fun wtf(tag: String, msg: String?, tr: Throwable?) {
            logger?.wtf(tag, msg, tr)
        }
    }
}