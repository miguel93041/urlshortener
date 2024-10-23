package es.unizar.urlshortener.core.usecases

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class RedirectionLimitUseCaseTest {

    private val repository = InMemoryRedirectionCountRepository()
    private val useCase = RedirectionLimitUseCaseImpl(3, repository)

    @Test
    fun `should limit redirections when limit is reached`() {
        val urlId = "abc123"

        useCase.incrementRedirectionCount(urlId)
        useCase.incrementRedirectionCount(urlId)
        useCase.incrementRedirectionCount(urlId)


        assertFalse(useCase.checkRedirectionLimit(urlId))  // Debería devolver false ya que el límite es 3
    }
}