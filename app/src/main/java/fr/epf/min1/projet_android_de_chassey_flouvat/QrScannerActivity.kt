package fr.epf.min1.projet_android_de_chassey_flouvat

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QrScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("QR_DEBUG", "onCreate appelé")
        Toast.makeText(this, "Activity lancée", Toast.LENGTH_SHORT).show()

        if (checkSelfPermission(android.Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
            return
        }

        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun handleResult(result: Result?) {
        val content = result?.text.orEmpty()

        if (content.startsWith("PRODUCT_ID:")) {
            val productId = content.removePrefix("PRODUCT_ID:").toIntOrNull()
            if (productId != null) {
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra("PRODUCT_ID", productId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "ID produit invalide", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "QR Code non reconnu", Toast.LENGTH_SHORT).show()
        }

        finish() // ferme l'activité après scan
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            recreate()
        } else {
            Toast.makeText(this, "Permission caméra refusée", Toast.LENGTH_SHORT).show()
            finish()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
