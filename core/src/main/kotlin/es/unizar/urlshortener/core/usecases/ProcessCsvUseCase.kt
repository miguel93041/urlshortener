package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.ShortUrlProperties
import java.io.InputStreamReader
import java.io.StringWriter

interface ProcessCsvUseCase {
    fun processCsv(reader: InputStreamReader, createShortUrlUseCase: CreateShortUrlUseCase): String
}

class ProcessCsvUseCaseImpl (
    val baseUrl: String // por ejemplo: "http://localhost:8080"
) : ProcessCsvUseCase {
    override fun processCsv(reader: InputStreamReader, createShortUrlUseCase: CreateShortUrlUseCase): String {
        val result = StringWriter()

        // Cabecera del nuevo CSV
        result.append("url normal,url acortada\n")

        reader.use { br ->
            br.forEachLine { line ->
                val originalUrl = line.trim()
                try {
                    // Acortar la URL
                    val shortUrl = createShortUrlUseCase.create(originalUrl, ShortUrlProperties()) // se crea el hash
                    val shortenedUrl = "$baseUrl/${shortUrl.hash}" // con la base se forma la url completa
                    result.append("$originalUrl,$shortenedUrl\n") // para meterla en el fichero
                } catch (e: Exception) {
                    // Manejar excepciones o errores en el acortamiento
                    result.append("$originalUrl,ERROR: ${e.message}\n")
                }
            }
        }

        return result.toString()
    }
}
