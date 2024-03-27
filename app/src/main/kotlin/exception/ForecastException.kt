package hu.vanio.kotlin.feladat.ms.exception

class ForecastException(
        message: String? = null,
        cause: Throwable? = null): Exception(message, cause)