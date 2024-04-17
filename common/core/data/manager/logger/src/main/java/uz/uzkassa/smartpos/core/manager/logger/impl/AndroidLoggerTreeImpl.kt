package uz.uzkassa.smartpos.core.manager.logger.impl

import android.util.Log
//import uz.uzkassa.smartpos.core.manager.logger.BuildConfig
import uz.uzkassa.smartpos.core.manager.logger.LoggerTree

internal object AndroidLoggerTreeImpl : LoggerTree(false) {

    override fun v(tag: String, msg: String?) {
        Log.v(tag, msg ?: "null")
    }

    override fun v(tag: String, msg: String?, tr: Throwable?) {
        Log.v(tag, msg ?: "null", tr)
    }

    override fun d(tag: String, msg: String?) {
        Log.d(tag, msg ?: "null")
    }

    override fun d(tag: String, msg: String?, tr: Throwable?) {
        Log.d(tag, msg ?: "null", tr)
    }

    override fun i(tag: String, msg: String?) {
        Log.i(tag, msg ?: "null")
    }

    override fun i(tag: String, msg: String?, tr: Throwable?) {
        Log.i(tag, msg ?: "null", tr)
    }

    override fun w(tag: String, msg: String?) {
        Log.w(tag, msg ?: "null")
    }

    override fun w(tag: String, msg: String?, tr: Throwable?) {
        Log.w(tag, msg ?: "null", tr)
    }

    override fun w(tag: String, tr: Throwable?) {
        Log.w(tag, tr)
    }

    override fun e(tag: String, msg: String?) {
        Log.e(tag, msg ?: "null")
    }

    override fun e(tag: String, msg: String?, tr: Throwable?) {
        Log.e(tag, msg ?: "null", tr)
    }

    override fun wtf(tag: String, msg: String?) {
        Log.wtf(tag, msg ?: "null")
    }

    override fun wtf(tag: String, tr: Throwable?) {
        if (tr != null) Log.wtf(tag, tr) else Log.wtf(tag, "null")
    }

    override fun wtf(tag: String, msg: String?, tr: Throwable?) {
        Log.wtf(tag, msg ?: "null", tr)
    }
}