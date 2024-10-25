package es.unizar.urlshortener.core.usecases

import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import es.unizar.urlshortener.core.InvalidUrlException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import java.io.ByteArrayOutputStream
import kotlin.test.Test

class CreateQRUseCaseImplTest {
    private val qrCodeWriter: QRCodeWriter = mock()
    private val byteArrayOutputStream: ByteArrayOutputStream = mock()
    private val createQRUseCase = CreateQRUseCaseImpl(qrCodeWriter, byteArrayOutputStream)

    @Test
    fun `should create QR code and return byte array`() {
        val url = "https://example.com"
        val size = 250
        val bitMatrix = BitMatrix(size, size)

        whenever(qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size)).thenReturn(bitMatrix)

        val byteArray = byteArrayOf(1, 2, 3, 4)
        whenever(byteArrayOutputStream.toByteArray()).thenReturn(byteArray)

        val result = createQRUseCase.create(url, size)

        assertEquals(byteArray.toList(), result.toList())
        verify(qrCodeWriter).encode(url, BarcodeFormat.QR_CODE, size, size)
    }

    @Test
    fun `should throw InvalidUrlException when URL is invalid`() {
        val invalidUrl = ""
        val size = 250

        assertThrows<InvalidUrlException> {
            createQRUseCase.create(invalidUrl, size)
        }
    }
}
