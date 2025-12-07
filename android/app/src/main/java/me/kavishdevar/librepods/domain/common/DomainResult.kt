package me.kavishdevar.librepods.domain.common

/**
 * Generic result type for domain usecases. Keep Android types out of domain.
 */
sealed class DomainResult<out T> {
    data class Success<T>(val value: T) : DomainResult<T>()
    data class Error(val cause: Throwable? = null, val message: String? = cause?.message) : DomainResult<Nothing>()
}
