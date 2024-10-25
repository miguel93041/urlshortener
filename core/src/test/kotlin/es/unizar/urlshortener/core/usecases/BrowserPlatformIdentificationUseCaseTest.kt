package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.BrowserPlatform
import es.unizar.urlshortener.core.InvalidUrlException
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import ua_parser.Client
import ua_parser.OS
import ua_parser.Parser
import ua_parser.UserAgent
import kotlin.test.Test
import kotlin.test.assertEquals

class BrowserPlatformIdentificationUseCaseTest {
    private val parser: Parser = mock()
    private val browserPlatformIdentificationUseCase = BrowserPlatformIdentificationUseCaseImpl(parser)

    @Test
    fun `should return correct browser and platform when userAgent is valid`() {
        val userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"

        val userAgent = UserAgent("Chrome", "91", "0", "4472")
        val os = OS("Windows", "10", "", "", "")
        val client = Client(userAgent, os, null)

        whenever(parser.parse(userAgentString)).thenReturn(client)

        val result = browserPlatformIdentificationUseCase.parse(userAgentString)

        assertEquals(BrowserPlatform("Chrome", "Windows"), result)
    }

    @Test
    fun `should throw InvalidUrlException when userAgent is invalid`() {
        val invalidUserAgentString = ""

        assertThrows(InvalidUrlException::class.java) {
            browserPlatformIdentificationUseCase.parse(invalidUserAgentString)
        }
    }

    @Test
    fun `should return default values when parser returns null fields`() {
        val userAgentString = "Unknown/0.0"

        val userAgent = UserAgent(null, null, null, null)
        val os = OS(null, null, null, null, null)
        val client = Client(userAgent, os, null)

        whenever(parser.parse(userAgentString)).thenReturn(client)

        val result = browserPlatformIdentificationUseCase.parse(userAgentString)

        assertEquals(BrowserPlatform("Unknown Browser", "Unknown Platform"), result)
    }
}
