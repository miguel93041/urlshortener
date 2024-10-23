package es.unizar.urlshortener.core.usecases

interface RedirectionLimitUseCase {
    fun checkRedirectionLimit(urlId: String): Boolean
    fun incrementRedirectionCount(urlId: String)
}

interface RedirectionCountRepository {
    fun getCount(urlId: String): Int?
    fun incrementCount(urlId: String)
}

class RedirectionLimitUseCaseImpl(
    val redirectionLimit: Int = 10,
    val redirectionCountRepository: RedirectionCountRepository
) : RedirectionLimitUseCase {

    override fun checkRedirectionLimit(urlId: String): Boolean {
        val currentCount = redirectionCountRepository.getCount(urlId) ?: 0
        return currentCount < redirectionLimit
    }

    override fun incrementRedirectionCount(urlId: String) {
        redirectionCountRepository.incrementCount(urlId)
    }
}