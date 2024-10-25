package es.unizar.urlshortener

import com.google.zxing.qrcode.QRCodeWriter
import es.unizar.urlshortener.core.GeoLocationService
import es.unizar.urlshortener.core.usecases.*
import es.unizar.urlshortener.infrastructure.delivery.HashServiceImpl
import es.unizar.urlshortener.infrastructure.delivery.ValidatorServiceImpl
import es.unizar.urlshortener.infrastructure.repositories.ClickEntityRepository
import es.unizar.urlshortener.infrastructure.repositories.ClickRepositoryServiceImpl
import es.unizar.urlshortener.infrastructure.repositories.ShortUrlEntityRepository
import es.unizar.urlshortener.infrastructure.repositories.ShortUrlRepositoryServiceImpl
import es.unizar.urlshortener.thirdparties.ipinfo.GeoLocationServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import ua_parser.Parser
import java.io.ByteArrayOutputStream

/**
 * Wires use cases with service implementations, and services implementations with repositories.
 *
 * **Note**: Spring Boot is able to discover this [Configuration] without further configuration.
 */
@Configuration
class ApplicationConfiguration(
    @Autowired val shortUrlEntityRepository: ShortUrlEntityRepository,
    @Autowired val clickEntityRepository: ClickEntityRepository
) {
    /**
     * Provides an implementation of the ClickRepositoryService.
     * @return an instance of ClickRepositoryServiceImpl.
     */
    @Bean
    fun clickRepositoryService() = ClickRepositoryServiceImpl(clickEntityRepository)

    /**
     * Provides an implementation of the ShortUrlRepositoryService.
     * @return an instance of ShortUrlRepositoryServiceImpl.
     */
    @Bean
    fun shortUrlRepositoryService() = ShortUrlRepositoryServiceImpl(shortUrlEntityRepository)


    /**
     * Provides an implementation of the ValidatorService.
     * @return an instance of ValidatorServiceImpl.
     */
    @Bean
    fun validatorService() = ValidatorServiceImpl()

    /**
     * Provides an implementation of the HashService.
     * @return an instance of HashServiceImpl.
     */
    @Bean
    fun hashService() = HashServiceImpl()

    /**
     * Provides an implementation of the RedirectUseCase.
     * @return an instance of RedirectUseCaseImpl.
     */
    @Bean
    fun redirectUseCase() = RedirectUseCaseImpl(shortUrlRepositoryService())

    /**
     * Provides an implementation of the LogClickUseCase.
     * @return an instance of LogClickUseCaseImpl.
     */
    @Bean
    fun logClickUseCase() = LogClickUseCaseImpl(clickRepositoryService())

    /**
     * Provides an implementation of the CreateShortUrlUseCase.
     * @return an instance of CreateShortUrlUseCaseImpl.
     */
    @Bean
    fun createShortUrlUseCase() =
        CreateShortUrlUseCaseImpl(shortUrlRepositoryService(), validatorService(), hashService())

    @Bean
    fun qrCodeWriter(): QRCodeWriter = QRCodeWriter()

    @Bean
    fun byteArrayOutputStream(): ByteArrayOutputStream = ByteArrayOutputStream()

    /**
     * Provides an implementation of the LogClickUseCase.
     * @return an instance of LogClickUseCaseImpl.
     */
    @Bean
    fun createQRUseCase(
        qrCodeWriter: QRCodeWriter,
        byteArrayOutputStream: ByteArrayOutputStream
    ) = CreateQRUseCaseImpl(qrCodeWriter, byteArrayOutputStream)

    @Bean
    fun ProcessCsvUseCase() = ProcessCsvUseCaseImpl("http://localhost:8080")

    @Bean
    fun redirectionCountRepository(): RedirectionCountRepository {
        return InMemoryRedirectionCountRepository()
    }

    @Bean
    fun redirectionLimitUseCase(redirectionCountRepository: RedirectionCountRepository): RedirectionLimitUseCase {
        return RedirectionLimitUseCaseImpl(redirectionLimit = 10, redirectionCountRepository)
    }

    @Bean
    fun webClient(): WebClient = WebClient.builder().build()

    @Bean
    fun geoLocationService(webClient: WebClient): GeoLocationService {
        return GeoLocationServiceImpl(webClient)
    }

    @Bean
    fun uaParser(): Parser = Parser()

    @Bean
    fun browserPlatformIdentificationUseCase(uaParser: Parser): BrowserPlatformIdentificationUseCase =
        BrowserPlatformIdentificationUseCaseImpl(uaParser)

    @Bean
    fun urlAccesibilityCheckUseCase(webClient: WebClient): UrlAccessibilityCheckUseCase =
        UrlAccessibilityCheckUseCaseImpl(webClient)
}
