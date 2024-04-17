package uz.uzkassa.smartpos.core.presentation.app.activity.anr

internal object AnrSupervisorKt {
    private val anrSupervisor: AnrSupervisor = AnrSupervisor()

    fun start() =
        anrSupervisor.start()

    fun stop() =
        anrSupervisor.stop()
}