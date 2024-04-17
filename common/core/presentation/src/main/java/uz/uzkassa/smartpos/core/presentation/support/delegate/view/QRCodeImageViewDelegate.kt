package uz.uzkassa.smartpos.core.presentation.support.delegate.view

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.*

class QRCodeImageViewDelegate(target: Fragment) : ViewDelegate<ImageView>(target) {
    private val delegateScope = CoroutineScope(context = Dispatchers.Main + Job())

    override fun onDestroy() {
        delegateScope.cancel()
        super.onDestroy()
    }

    fun generateQrCode(value: String) {
        delegateScope.launch {
            val bitmap: Bitmap? = withContext(Dispatchers.IO) { generateQrCodeBitmap(value) }
            bitmap?.let {
                view?.setImageBitmap(it)
            }
        }
    }

    private fun generateQrCodeBitmap(content: String): Bitmap? {
        val writer = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix =
                writer.encode(content, BarcodeFormat.QR_CODE, 200, 200)

            val width: Int = bitMatrix.width
            val height: Int = bitMatrix.height
            val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x: Int in 0 until width)
                for (y: Int in 0 until height)
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)

            return bitmap
        } catch (e: WriterException) {
            return null
        }
    }
}