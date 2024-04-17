package uz.uzkassa.smartpos.core.utils.result


@Suppress("UNCHECKED_CAST")
inline fun <R, T: Result<R>> T.mapFailure(transform: (Throwable) -> Throwable): T {
    return when {
        isSuccess -> Result.success(getOrThrow())
        else -> Result.failure(transform(checkNotNull(exceptionOrNull())))
    } as T
}