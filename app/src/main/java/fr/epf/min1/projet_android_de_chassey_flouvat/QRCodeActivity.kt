package fr.epf.min1.projet_android_de_chassey_flouvat

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import fr.epf.min1.projet_android_de_chassey_flouvat.data.Product
import fr.epf.min1.projet_android_de_chassey_flouvat.repository.ProductRepository

class QRCodeActivity : AppCompatActivity() {

    private lateinit var qrCodeImageView: ImageView
    private lateinit var productInfoText: TextView
    private lateinit var backButton: Button
    private lateinit var scanButton: Button

    private var currentProduct: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)

        initViews()
        setupButtons()

        // Récupérer l'ID du produit
        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId != -1) {
            loadProductAndGenerateQR(productId)
        } else {
            Toast.makeText(this, "Erreur: ID produit manquant", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initViews() {
        qrCodeImageView = findViewById(R.id.qrCodeImageView)
        backButton = findViewById(R.id.backButton)
        scanButton = findViewById(R.id.scanButton)
    }

    private fun setupButtons() {
        backButton.setOnClickListener {
            finish()
        }

        scanButton.setOnClickListener {
            val intent = Intent(this, QrScannerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadProductAndGenerateQR(productId: Int) {
        val repository = ProductRepository()
        repository.getProductById(productId).observe(this) { product ->
            if (product != null) {
                generateQRCode(productId)
            } else {
                Toast.makeText(this, "Produit introuvable", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    private fun generateQRCode(productId: Int) {
        try {
            // Créer l'URL qui sera dans le QR code
            val qrContent = "PRODUCT_ID:$productId"

            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(qrContent, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }

            qrCodeImageView.setImageBitmap(bitmap)

        } catch (e: WriterException) {
            Toast.makeText(this, "Erreur génération QR Code", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}