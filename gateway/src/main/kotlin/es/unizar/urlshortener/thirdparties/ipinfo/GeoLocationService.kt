package es.unizar.urlshortener.thirdparties.ipinfo

import es.unizar.urlshortener.core.GeoLocation
import es.unizar.urlshortener.core.GeoLocationService
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class GeoLocationServiceImpl(
    private val webClient: WebClient,
    dotenv: Dotenv
) : GeoLocationService {

    private val accessToken = dotenv[DOTENV_IPINFO_KEY]


    // TODO: Custom IP class that validates and checks for IPv4 or IPv6 format
    override fun get(ip: String): GeoLocation {
        val url = buildRequestUrl(ip)

        // TODO: In the custom IP class auto-detect if ip is Bogon and return
        val response = webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(Map::class.java)
            .block()

        val ipAddress = response?.get("ip") as String
        val country = if (response.containsKey("bogon")) {
            "Bogon"
        } else {
            response["country"] as String
        }

        return GeoLocation(ipAddress, country)
    }

    // TODO: Adapt request URL to IP format (If IP4 or IP6 IPInfo endpoint differs)
    private fun buildRequestUrl(ip: String): String {
        return "${IPINFO_BASE_URL}$ip?token=$accessToken"
    }

    companion object {
        const val DOTENV_IPINFO_KEY = "IPINFO_API_KEY"
        const val IPINFO_BASE_URL = "https://ipinfo.io/"
    }
}
