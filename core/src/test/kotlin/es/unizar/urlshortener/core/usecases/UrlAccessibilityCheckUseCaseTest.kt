package es.unizar.urlshortener.core.usecases

import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UrlAccessibilityCheckUseCaseTest {
    private lateinit var webClient: WebClient
    private lateinit var requestHeadersUriSpec: WebClient.RequestHeadersUriSpec<*>
    private lateinit var requestHeadersSpec: WebClient.RequestHeadersSpec<*>
    private lateinit var responseSpec: WebClient.ResponseSpec

    private lateinit var useCase: UrlAccessibilityCheckUseCaseImpl

    @BeforeEach
    fun setUp() {
        webClient = mock(WebClient::class.java)
        requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec::class.java)
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec::class.java)
        responseSpec = mock(WebClient.ResponseSpec::class.java)

        useCase = UrlAccessibilityCheckUseCaseImpl(webClient)
    }

    @Test
    fun `test URL is reachable returns true`() {
        `when`(webClient.get()).thenReturn(requestHeadersUriSpec)
        `when`(requestHeadersUriSpec.uri("http://valid.url")).thenReturn(requestHeadersSpec)
        `when`(requestHeadersSpec.retrieve()).thenReturn(responseSpec)
        `when`(responseSpec.toBodilessEntity()).thenReturn(Mono.just(ResponseEntity.ok().build()))

        val result = useCase.isUrlReachable("http://valid.url")
        assertTrue(result, "The URL should be reachable")
    }

    @Test
    fun `test URL is not reachable returns false`() {
        `when`(webClient.get()).thenReturn(requestHeadersUriSpec)
        `when`(requestHeadersUriSpec.uri("http://notFound.url")).thenReturn(requestHeadersSpec)
        `when`(requestHeadersSpec.retrieve()).thenThrow(WebClientResponseException::class.java)

        val result = useCase.isUrlReachable("http://notFound.url")
        assertFalse(result, "The URL should not be reachable (404 Not Found)")
    }
}
