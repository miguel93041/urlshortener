package es.unizar.urlshortener.thirdparties.ipinfo

import io.github.cdimascio.dotenv.Dotenv
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class UrlSafetyServiceImplTest {

    @Mock
    private lateinit var webClient: WebClient

    @Mock
    private lateinit var webClientBuilder: WebClient.Builder

    @Mock
    private lateinit var requestBodyUriSpec: WebClient.RequestBodyUriSpec

    @Mock
    private lateinit var responseSpec: WebClient.ResponseSpec

    @Mock
    private lateinit var dotenv: Dotenv

    @InjectMocks
    private lateinit var urlSafetyService: UrlSafetyServiceImpl

    private val testUrl = "http://example.com"
    private val testApiKey = "test-api-key"

    @Test
    fun `isSafe should return true when API response has no matches`() {
        val responseBody = emptyMap<String, Any>()
        mockWebClientPostResponse(responseBody)

        val result = urlSafetyService.isSafe(testUrl)

        assertTrue(result)
    }

    @Test
    fun `isSafe should return false when API response contains matches`() {
        val responseBody = mapOf("matches" to listOf(mapOf("threatType" to "MALWARE")))
        mockWebClientPostResponse(responseBody)

        val result = urlSafetyService.isSafe(testUrl)

        assertFalse(result)
    }

    private fun mockWebClientPostResponse(response: Map<String, Any>) {
        val requestBodyUriSpec = Mockito.mock(WebClient.RequestBodyUriSpec::class.java)
        val requestBodySpec = Mockito.mock(WebClient.RequestBodySpec::class.java)
        val responseSpec = Mockito.mock(WebClient.ResponseSpec::class.java)

        // Mock para `post()` y `uri()` de WebClient
        Mockito.`when`(webClient.post()).thenReturn(requestBodyUriSpec)
        Mockito.`when`(requestBodyUriSpec.uri(Mockito.anyString())).thenReturn(requestBodySpec)

        // Mock para `bodyValue()` que devuelve `requestBodySpec`
        Mockito.`when`(requestBodySpec.bodyValue(Mockito.any())).thenReturn(requestBodySpec)

        // Mock para `retrieve()` que devuelve `responseSpec`
        Mockito.`when`(requestBodySpec.retrieve()).thenReturn(responseSpec)

        // Mock para `onStatus()` que devuelve el propio `responseSpec` para continuar con el flujo
        Mockito.`when`(responseSpec.onStatus(Mockito.any(), Mockito.any())).thenReturn(responseSpec)

        // Mock para `bodyToMono()` que devuelve directamente un `Mono` con la respuesta deseada
        Mockito.`when`(responseSpec.bodyToMono(Map::class.java)).thenReturn(Mono.just(response))
    }

}
