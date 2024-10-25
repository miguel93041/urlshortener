package es.unizar.urlshortener.core

import org.springframework.web.servlet.support.ServletUriComponentsBuilder

/**
 * Interface for providing the base URL used for generating shortened URLs.
 */
interface BaseUrlProvider {
    /**
     * Returns the base URL for the current request context.
     *
     * @return The base URL as a String.
     */
    fun get(): String
}

/**
 * Default implementation of the BaseUrlProvider interface.
 * Provides the base URL for the current request using ServletUriComponentsBuilder.
 */
class BaseUrlProviderImpl: BaseUrlProvider {
    /**
     * Retrieves the base URL for the current request context.
     *
     * @return The base URL as a String.
     */
    override fun get(): String {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
    }
}
