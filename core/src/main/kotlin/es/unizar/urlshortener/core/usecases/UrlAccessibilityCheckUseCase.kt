package es.unizar.urlshortener.core.usecases

import org.springframework.web.reactive.function.client.WebClient

/**
 * Interface for checking the accessibility of a given URL.
 */
interface UrlAccessibilityCheckUseCase {
    /**
     * Verifies if a URL is reachable.
     *
     * @param url The URL to check.
     * @return True if the URL is reachable, false otherwise.
     */
    fun isUrlReachable(url: String): Boolean
}

/**
 * Implementation of [UrlAccessibilityCheckUseCase]
 */
@Suppress("TooGenericExceptionCaught", "SwallowedException")
class UrlAccessibilityCheckUseCaseImpl(
    private val webClient: WebClient
) : UrlAccessibilityCheckUseCase {
    /**
     * Verifies if a URL is reachable by making a GET request to the URL.
     *
     * @param url The URL to check.
     * @return True if the URL is reachable, false otherwise.
     * @throws Exception If an error occurs during the request, the method will catch it and return false.
     */
    override fun isUrlReachable(url: String): Boolean {
        return try {
            webClient.get()
                .uri(url)
                .retrieve()
                .toBodilessEntity()
                .block()

            true
        } catch (e: Exception) {
            false
        }
    }
}
