package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.Redirection
import es.unizar.urlshortener.core.ShortUrl
import es.unizar.urlshortener.core.ShortUrlProperties
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import kotlin.test.assertEquals

class ProcessCsvUseCaseTest {

    private val baseUrl = "http://localhost:8080"
    private val createShortUrlUseCase = mock<CreateShortUrlUseCase>()

    private val processCsvUseCase = ProcessCsvUseCaseImpl(baseUrl)

    @Test
    fun `processCsv generates correct CSV for valid URLs`() {
        val redirection = Redirection(target = "http://example.com")

        whenever(createShortUrlUseCase.create("http://example.com", ShortUrlProperties()))
            .thenReturn(ShortUrl("f684a3c4", redirection))

        val csvContent = "http://example.com\nhttp://example.com"
        val inputStream = ByteArrayInputStream(csvContent.toByteArray())
        val reader = InputStreamReader(inputStream)

        val result = processCsvUseCase.processCsv(reader, createShortUrlUseCase)

        val expectedOutput = "url normal,url acortada\n" +
                "http://example.com,http://localhost:8080/f684a3c4\n" +
                "http://example.com,http://localhost:8080/f684a3c4\n"

        assertEquals(expectedOutput, result)
    }

    @Test
    fun `processCsv marks errors when URL shortening fails`() {
        whenever(createShortUrlUseCase.create("invalid_url", ShortUrlProperties()))
            .thenThrow(RuntimeException("Invalid URL"))

        val csvContent = "invalid_url\n"
        val inputStream = ByteArrayInputStream(csvContent.toByteArray())
        val reader = InputStreamReader(inputStream)

        val result = processCsvUseCase.processCsv(reader, createShortUrlUseCase)

        val expectedOutput = "url normal,url acortada\n" +
                "invalid_url,ERROR: Invalid URL\n"

        assertEquals(expectedOutput, result)
    }

    @Test
    fun `processCsv handles multiple URLs with mixed valid and invalid inputs`() {
        val redirection = Redirection(target = "http://example.com")
        whenever(createShortUrlUseCase.create("http://example.com", ShortUrlProperties()))
            .thenReturn(ShortUrl("f684a3c4", redirection))
        whenever(createShortUrlUseCase.create("invalid_url", ShortUrlProperties()))
            .thenThrow(RuntimeException("Invalid URL"))

        val csvContent = "http://example.com\ninvalid_url\nhttp://example.com"
        val inputStream = ByteArrayInputStream(csvContent.toByteArray())
        val reader = InputStreamReader(inputStream)

        val result = processCsvUseCase.processCsv(reader, createShortUrlUseCase)

        val expectedOutput = "url normal,url acortada\n" +
                "http://example.com,http://localhost:8080/f684a3c4\n" +
                "invalid_url,ERROR: Invalid URL\n" +
                "http://example.com,http://localhost:8080/f684a3c4\n"

        assertEquals(expectedOutput, result)
    }
}
