package uz.uzkassa.smartpos.core.data.source.gtpos.intent

import android.content.Intent

interface GTPOSLaunchIntent {

    fun intentOrThrow(type: Type = Type.DEFAULT): Intent

    enum class Type {
        DEFAULT,
        USER
    }
}