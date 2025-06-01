package fr.epf.min1.projet_android_de_chassey_flouvat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.epf.min1.projet_android_de_chassey_flouvat.data.Product
import fr.epf.min1.projet_android_de_chassey_flouvat.viewmodel.ProductViewModel
import fr.epf.min1.projet_android_de_chassey_flouvat.data.CartManager

class ProductDetailActivity : BaseActivity() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var productImage: ImageView
    private lateinit var productTitle: TextView
    private lateinit var productPrice: TextView
    private lateinit var productCategory: TextView
    private lateinit var productDescription: TextView
    private lateinit var addToCartButton: Button
    private lateinit var qrCodeButton: Button


    private var currentProduct: Product? = null

    override fun getBottomNavItemId(): Int = R.id.Home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_product_detail)

        initViews()


        //ViewModel
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        // id du produit
        val productId = intent.getIntExtra("PRODUCT_ID", -1)

        if (productId != -1) {
            loadProductDetails(productId)
        } else {
            Toast.makeText(this, "Erreur: Produit introuvable", Toast.LENGTH_SHORT).show()
            finish()
        }

        //bouton ajouter au panier
        addToCartButton.setOnClickListener {
            currentProduct?.let { product ->
                CartManager.addProduct(product)
                Toast.makeText(this, "Produit ajouté au panier !", Toast.LENGTH_SHORT).show()
            }
        }
        qrCodeButton.setOnClickListener {
            val productId = intent.getIntExtra("PRODUCT_ID", -1)
            if (productId != -1) {
                val intent = Intent(this, QRCodeActivity::class.java)
                intent.putExtra("PRODUCT_ID", productId)
                startActivity(intent)
            }
        }

    }

        private fun initViews() {
            productImage = findViewById(R.id.productImage)
            productTitle = findViewById(R.id.productTitle)
            productPrice = findViewById(R.id.productPrice)
            productCategory = findViewById(R.id.productCategory)
            productDescription = findViewById(R.id.productDescription)
            addToCartButton = findViewById(R.id.addToCartButton)
            qrCodeButton = findViewById(R.id.qrCodeButton)
        }

        private fun loadProductDetails(productId: Int) {
            val repository =
                fr.epf.min1.projet_android_de_chassey_flouvat.repository.ProductRepository()

            repository.getProductById(productId).observe(this) { product ->
                if (product != null) {
                    displayProduct(product)
                } else {
                    Toast.makeText(this, "Erreur lors du chargement du produit", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
        }

        private fun displayProduct(product: Product) {
            currentProduct = product  // AJOUTER cette ligne

            productTitle.text = product.title
            productPrice.text = "€${product.price}"
            productCategory.text = product.category
            productDescription.text = product.description

            Glide.with(this)
                .load(product.image)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(productImage)
        }

}