package es.unizar.urlshortener.core.usecases

class InMemoryRedirectionCountRepository : RedirectionCountRepository {
    private val counterMap = mutableMapOf<String, Int>()

    override fun getCount(urlId: String): Int? = counterMap[urlId]

    override fun incrementCount(urlId: String) {
        counterMap[urlId] = counterMap.getOrDefault(urlId, 0) + 1
    }
}
