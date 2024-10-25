@file:Suppress("WildcardImport")
package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.*
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.io.StringReader
import java.io.StringWriter
import java.time.OffsetDateTime

class ProcessCsvUseCaseTest {
    private val createShortUrlUseCase = mock(CreateShortUrlUseCase::class.java)
    private val baseUrlProvider = mock(BaseUrlProvider::class.java)
    private val geoLocation = mock(GeoLocationService::class.java)
    private val urlAccessibilityCheckUseCase = mock(UrlAccessibilityCheckUseCase::class.java)
    private val processCsvUseCase = ProcessCsvUseCaseImpl(createShortUrlUseCase, baseUrlProvider, geoLocation, urlAccessibilityCheckUseCase)
    private val baseUrl = "http://localhost:8080"
    private val request = mock(HttpServletRequest::class.java)

    @BeforeEach
    fun setUp() {
        `when`(baseUrlProvider.get()).thenReturn(baseUrl)
    }

    @Test
    fun `processCsv should process a valid URL correctly`() {
        val input = "http://example.com"
        val reader = StringReader(input)
        val writer = StringWriter()

        // Mock the behavior of createShortUrlUseCase
        val redirection = Redirection(target = input)
        val shortUrl = ShortUrl(
            hash = "abc123",
            redirection = redirection,
            created = OffsetDateTime.now(),
            properties = ShortUrlProperties()
        )
        `when`(createShortUrlUseCase.create(input, ShortUrlProperties())).thenReturn(shortUrl)

        processCsvUseCase.processCsv(reader, writer, request)

        val expectedOutput = "original-url,shortened-url\nhttp://example.com,${baseUrl}/abc123\n"
        assertEquals(expectedOutput, writer.toString())
    }

    @Test
    fun `processCsv should handle invalid URL`() {
        val input = "invalid-url"
        val reader = StringReader(input)
        val writer = StringWriter()

        // Mock the behavior of createShortUrlUseCase to throw an exception for an invalid URL
        `when`(createShortUrlUseCase.create(input, ShortUrlProperties()))
            .thenThrow(IllegalArgumentException("Invalid URL"))

        processCsvUseCase.processCsv(reader, writer, request)

        val expectedOutput = "original-url,shortened-url\ninvalid-url,ERROR: Invalid URL\n"
        assertEquals(expectedOutput, writer.toString())
    }

    @Test
    fun `processCsv should handle empty input`() {
        val reader = StringReader("")
        val writer = StringWriter()

        processCsvUseCase.processCsv(reader, writer, request)

        val expectedOutput = "original-url,shortened-url\n"
        assertEquals(expectedOutput, writer.toString())
    }

    @Test
    fun `processCsv should process multiple URLs correctly`() {
        val input = """
            http://example.com
            invalid-url
            http://another-example.com
        """.trimIndent()
        val reader = StringReader(input)
        val writer = StringWriter()

        val exampleRedirection = Redirection(target = "http://example.com")
        val anotherRedirection = Redirection(target = "http://another-example.com")

        val shortUrl1 = ShortUrl(
            hash = "abc123",
            redirection = exampleRedirection,
            created = OffsetDateTime.now(),
            properties = ShortUrlProperties()
        )
        val shortUrl2 = ShortUrl(
            hash = "xyz789",
            redirection = anotherRedirection,
            created = OffsetDateTime.now(),
            properties = ShortUrlProperties()
        )

        `when`(createShortUrlUseCase.create("http://example.com", ShortUrlProperties()))
            .thenReturn(shortUrl1)
        `when`(createShortUrlUseCase.create("invalid-url", ShortUrlProperties()))
            .thenThrow(IllegalArgumentException("Invalid URL"))
        `when`(createShortUrlUseCase.create("http://another-example.com", ShortUrlProperties()))
            .thenReturn(shortUrl2)

        processCsvUseCase.processCsv(reader, writer, request)

        val expectedOutput = """
            original-url,shortened-url
            http://example.com,${baseUrl}/abc123
            invalid-url,ERROR: Invalid URL
            http://another-example.com,${baseUrl}/xyz789
        """.trimIndent() + "\n"

        assertEquals(expectedOutput, writer.toString())
    }
}
