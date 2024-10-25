package es.unizar.urlshortener.core.usecases

import org.springframework.web.reactive.function.client.WebClient

/**
 * Given an url returns if it is reachable.
 */
interface UrlAccessibilityCheckUseCase {
    /**
     * Ensures if an url is reachable.
     *
     * @param url The URL.
     * @return True if the URl is reachable and false otherwise.
     */
    fun isUrlReachable(url: String): Boolean
}

/**
 * Implementation of [UrlAccessibilityCheckUseCase].
 */
class UrlAccessibilityCheckUseCaseImpl(
    private val webClient: WebClient
) : UrlAccessibilityCheckUseCase {
    /**
     * Ensures if an url is reachable.
     *
     * @param url The URL.
     * @return True if the URl is reachable and false otherwise.
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
