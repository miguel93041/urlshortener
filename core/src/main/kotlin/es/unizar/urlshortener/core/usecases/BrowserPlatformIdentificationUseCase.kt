package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.BrowserPlatform
import es.unizar.urlshortener.core.InvalidUrlException
import ua_parser.Parser

/**
 * Given a user agent request returns its browser and platform.
 */
interface BrowserPlatformIdentificationUseCase {
    /**
     * Parse the user agent header used during redirection requests.
     *
     * @param userAgent The user agent header request.
     * @return The browser and platform used during redirection requests.
     */
    fun parse(userAgent: String): BrowserPlatform
}

/**
 * Implementation of [BrowserPlatformIdentificationUseCase].
 */
class BrowserPlatformIdentificationUseCaseImpl(
    private val parser: Parser
) : BrowserPlatformIdentificationUseCase {
    /**
     * Parse the user agent header used during redirection requests.
     *
     * @param userAgent The user agent header request.
     * @return The browser and platform used during redirection requests.
     * @throws InvalidUrlException if the URL is not valid.
     */
    override fun parse(userAgent: String): BrowserPlatform {
        if (userAgent.isBlank()) {
            throw InvalidUrlException("User-Agent header is invalid")
        }

        val client = parser.parse(userAgent)

        val browser = client.userAgent.family ?: "Unknown Browser"
        val platform = client.os.family ?: "Unknown Platform"

        return BrowserPlatform(browser, platform)
    }
}
