package es.unizar.urlshortener.core.usecases

/**
 * Implementation of [RedirectionCountRepository] that tracks the number of
 * redirections for each shortened URL.
 */
class InMemoryRedirectionCountRepository : RedirectionCountRepository {
    private val counterMap = mutableMapOf<String, Int>()

    /**
     * Retrieves the redirection count for a given URL identifier.
     *
     * @param urlId The identifier of the shortened URL.
     * @return The number of times the URL has been redirected, or null if not found.
     */
    override fun getCount(urlId: String): Int? = counterMap[urlId]

    /**
     * Increments the redirection count for the given URL identifier.
     *
     * @param urlId The identifier of the shortened URL.
     */
    override fun incrementCount(urlId: String) {
        counterMap[urlId] = counterMap.getOrDefault(urlId, 0) + 1
    }
}
