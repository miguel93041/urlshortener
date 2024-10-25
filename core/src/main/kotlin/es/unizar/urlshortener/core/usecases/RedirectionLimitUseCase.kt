package es.unizar.urlshortener.core.usecases

/**
 * Interface for handling redirection limits for shortened URLs.
 */
interface RedirectionLimitUseCase {
    /**
     * Checks whether the redirection limit has been reached for a specific URL identifier.
     *
     * @param urlId The identifier of the shortened URL.
     * @return True if the redirection limit is reached, false otherwise.
     */
    fun isRedirectionLimit(urlId: String): Boolean

    /**
     * Increments the redirection count for a given URL identifier.
     *
     * @param urlId The identifier of the shortened URL.
     */
    fun incrementRedirectionCount(urlId: String)
}

/**
 * Interface for tracking redirection counts in a repository.
 */
interface RedirectionCountRepository {
    /**
     * Retrieves the current redirection count for a given URL identifier.
     *
     * @param urlId The identifier of the shortened URL.
     * @return The redirection count, or null if no count exists.
     */
    fun getCount(urlId: String): Int?

    /**
     * Increments the redirection count for a given URL identifier.
     *
     * @param urlId The identifier of the shortened URL.
     */
    fun incrementCount(urlId: String)
}

/**
 * Implementation of [RedirectionLimitUseCase] that checks and updates
 * the redirection count for shortened URLs with a set limit.
 *
 * @param redirectionLimit The maximum number of allowed redirections (default is 10).
 * @param redirectionCountRepository The repository used to store and retrieve redirection counts.
 */
class RedirectionLimitUseCaseImpl(
    val redirectionLimit: Int = 10,
    val redirectionCountRepository: RedirectionCountRepository
) : RedirectionLimitUseCase {
    /**
     * Checks if the current redirection count for a URL identifier has reached or exceeded the limit.
     *
     * @param urlId The identifier of the shortened URL.
     * @return True if the redirection limit is reached, false otherwise.
     */
    override fun isRedirectionLimit(urlId: String): Boolean {
        val currentCount = redirectionCountRepository.getCount(urlId) ?: 0
        return currentCount >= redirectionLimit
    }

    /**
     * Increments the redirection count for the specified URL identifier.
     *
     * @param urlId The identifier of the shortened URL.
     */
    override fun incrementRedirectionCount(urlId: String) {
        redirectionCountRepository.incrementCount(urlId)
    }
}
