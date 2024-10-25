package es.unizar.urlshortener.core.usecases

data class UrlValidationResult(val isSafe: Boolean, val statusCode: Int)

interface UrlValidationService {
    fun validate(url: String): UrlValidationResult
}
