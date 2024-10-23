package es.unizar.urlshortener.thirdparties.ipinfo

import es.unizar.urlshortener.core.GeoLocation
import es.unizar.urlshortener.core.GeoLocationService
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class GeoLocationServiceImpl (
    private val webClient: WebClient
): GeoLocationService {

    private val dotenv = Dotenv.load()
    private val ipInfoUrl = "https://ipinfo.io/"
    private val accessToken = dotenv["IPINFO_API_KEY"]

    override fun get(ip: String): GeoLocation {
        val response = webClient.get()
            .uri("$ipInfoUrl$ip?token=$accessToken")
            .retrieve()
            .bodyToMono(Map::class.java)
            .block()

        val ipAddress = response?.get("ip") as String
        var country = "Bogon"
        if (!response.containsKey("bogon")) {
            country = response["country"] as String
        }

        return GeoLocation(ipAddress, country)
    }
}