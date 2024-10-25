package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.BrowserPlatform
import es.unizar.urlshortener.core.InvalidUrlException
import ua_parser.Parser

/**
 * Interface for identifying the browser and platform from a user agent string.
 */
interface BrowserPlatformIdentificationUseCase {
    /**
     * Parses the user agent string received in a redirection request to identify
     * the browser and platform being used.
     *
     * @param userAgent The user agent header from the request.
     * @return A BrowserPlatform object containing the identified browser and platform.
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
     * Parses the user agent string to identify the browser and platform.
     *
     * @param userAgent The user agent header from the request.
     * @return A BrowserPlatform object containing the identified browser and platform.
     * @throws InvalidUrlException if the provided user agent string is empty or invalid.
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
