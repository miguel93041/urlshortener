package es.unizar.urlshortener.core.usecases

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
    fun parse(userAgent: String): Pair<String, String>
}

/**
 * Implementation of [BrowserPlatformIdentificationUseCase].
 */
class BrowserPlatformIdentificationUseCaseImpl : BrowserPlatformIdentificationUseCase {
    /**
     * Parse the user agent header used during redirection requests.
     *
     * @param userAgent The user agent header request.
     * @return The browser and platform used during redirection requests.
     * @throws InvalidUrlException if the URL is not valid.
     */
    override fun parse(userAgent: String): Pair<String, String> {
        val parser = Parser()
        val client = parser.parse(userAgent)

        val browser = "${client.userAgent.family} ${client.userAgent.major}.${client.userAgent.minor}"
        val platform = client.os.family

        return browser to platform
    }
}
