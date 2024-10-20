@file:Suppress("WildcardImport")

package es.unizar.urlshortener.core.usecases

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import es.unizar.urlshortener.core.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.ByteArrayOutputStream

/**
 * Given a shortened url returns a base64 representation of the QR.
 */
interface CreateQRUseCase {
    /**
     * Creates a QR for the given shortened URL with the given size in pixels.
     *
     * @param url The shortened URL.
     * @param size The width and height of the QR
     * @return A base64 representation [String] of the QR.
     */
    fun create(url: String, size: Int): ByteArray
}

/**
 * Implementation of [CreateQRUseCase].
 */
class CreateQRUseCaseImpl : CreateQRUseCase {
    /**
     * Creates a short URL for the given URL and optional data.
     *
     * @param url The URL to be shortened.
     * @param data The optional properties for the short URL.
     * @return The created [ShortUrl] entity.
     * @throws InvalidUrlException if the URL is not valid.
     */
    override fun create(url: String, size: Int): ByteArray {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size)
        val bufferedImage: BufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix)
        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }
}
