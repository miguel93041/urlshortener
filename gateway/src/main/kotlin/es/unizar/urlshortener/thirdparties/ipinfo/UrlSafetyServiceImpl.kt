package es.unizar.urlshortener.thirdparties.ipinfo


import es.unizar.urlshortener.core.usecases.UrlValidationService
import es.unizar.urlshortener.core.usecases.UrlValidationResult
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Primary

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
@Primary
class UrlSafetyServiceImpl (
    private val webClient: WebClient,
    dotenv: Dotenv
) : UrlValidationService {

    private val apiKey = dotenv["GOOGLE_API_KEY"]


    override fun validate(url: String): UrlValidationResult {
        System.out.println(url)
        val response = webClient.post()
            .uri("https://safebrowsing.googleapis.com/v4/threatMatches:find?key=$apiKey")
            .bodyValue(createRequestBody(url))
            .retrieve()
            .onStatus({ status -> status.isError }) { response ->
                response.bodyToMono(String::class.java).flatMap { errorResponse ->
                    throw RuntimeException("Error from API: $errorResponse")
                }
            }
            .bodyToMono(Map::class.java)
            .block()
        System.out.println(response)
        return processResponse(response)
    }

    private fun createRequestBody(url: String): Map<String, Any> {
        return mapOf(
            "client" to mapOf(
                "clientId" to "shortener",
                "clientVersion" to "1.0"
            ),
            "threatInfo" to mapOf(
                "threatTypes" to listOf("MALWARE", "SOCIAL_ENGINEERING"),
                "platformTypes" to listOf("ANY_PLATFORM"),
                "threatEntryTypes" to listOf("URL"),
                "threatEntries" to listOf(mapOf("url" to url))
            )
        )
    }

    private fun processResponse(response: Map<*, *>?): UrlValidationResult {
        return if (response?.containsKey("matches") == true) {
            UrlValidationResult(isSafe = false, statusCode = 403)
        } else{
            UrlValidationResult(isSafe = true, statusCode = 201)
        }
    }
}
