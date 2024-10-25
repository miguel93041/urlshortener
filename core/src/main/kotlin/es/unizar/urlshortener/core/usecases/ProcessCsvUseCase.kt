package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.ShortUrlProperties
import java.io.InputStreamReader
import java.io.StringWriter

interface ProcessCsvUseCase {
    fun processCsv(reader: InputStreamReader, createShortUrlUseCase: CreateShortUrlUseCase): String
}

@Suppress("TooGenericExceptionCaught")
class ProcessCsvUseCaseImpl (
    val baseUrl: String
) : ProcessCsvUseCase {
    override fun processCsv(reader: InputStreamReader, createShortUrlUseCase: CreateShortUrlUseCase): String {
        val result = StringWriter()

        result.append("original-url,shortened-url\n")

        reader.use { br ->
            br.forEachLine { line ->
                val originalUrl = line.trim()
                try {
                    // Acortar la URL
                    val shortUrl = createShortUrlUseCase.create(originalUrl, ShortUrlProperties())
                    val shortenedUrl = "$baseUrl/${shortUrl.hash}"
                    result.append("$originalUrl,$shortenedUrl\n")
                } catch (e: Exception) {
                    result.append("$originalUrl,ERROR: ${e.message}\n")
                }
            }
        }

        return result.toString()
    }
}
