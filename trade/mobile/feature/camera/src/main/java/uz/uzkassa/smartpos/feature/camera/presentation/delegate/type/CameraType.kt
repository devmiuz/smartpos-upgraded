package uz.uzkassa.smartpos.feature.camera.presentation.delegate.type

enum class CameraType(internal val id: Int) {
    BACK(0), FRONT(1);

    companion object {

        fun valueOf(value: Int): CameraType =
            values().find { it.id == value } ?: BACK
    }
}