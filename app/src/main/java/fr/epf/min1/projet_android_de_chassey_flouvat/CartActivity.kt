package fr.epf.min1.projet_android_de_chassey_flouvat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.epf.min1.projet_android_de_chassey_flouvat.data.CartManager

class CartActivity : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var emptyCartLayout: LinearLayout
    private lateinit var cartFooter: LinearLayout
    private lateinit var totalPriceText: TextView
    private lateinit var backButton: Button
    private lateinit var clearCartButton: Button
    private lateinit var checkoutButton: Button

    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        initViews()
        setupRecyclerView()
        setupButtons()
        updateUI()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> {
                    if (this::class != MainActivity::class) {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    true
                }
                R.id.Search -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("focus_search", true)
                    startActivity(intent)
                    true
                }
                R.id.Scan -> {
                    true
                }
                R.id.Account -> {
                    true
                }
                else -> false
            }
        }

    }

    private fun initViews() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        emptyCartLayout = findViewById(R.id.emptyCartLayout)
        cartFooter = findViewById(R.id.cartFooter)
        totalPriceText = findViewById(R.id.totalPriceText)
        backButton = findViewById(R.id.backButton)
        clearCartButton = findViewById(R.id.clearCartButton)
        checkoutButton = findViewById(R.id.checkoutButton)
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            CartManager.getCartItems().toMutableList()
        ) {
            // Callback quand le panier est mis à jour
            updateUI()
        }

        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartRecyclerView.adapter = cartAdapter
    }

    private fun setupButtons() {
        // Bouton retour
        backButton.setOnClickListener {
            finish()
        }

        // Bouton vider le panier
        clearCartButton.setOnClickListener {
            CartManager.clearCart()
            cartAdapter.updateData(emptyList())
            updateUI()
            Toast.makeText(this, "Panier vidé", Toast.LENGTH_SHORT).show()
        }

        // Bouton commander
        checkoutButton.setOnClickListener {
            if (CartManager.getItemCount() > 0) {
                Toast.makeText(this, "Commande passée ! Total: €${"%.2f".format(CartManager.getTotalPrice())}", Toast.LENGTH_LONG).show()
                CartManager.clearCart()
                cartAdapter.updateData(emptyList())
                updateUI()
            }
        }
    }

    private fun updateUI() {
        val cartItems = CartManager.getCartItems()
        val isEmpty = cartItems.isEmpty()

        // Afficher/masquer les éléments selon si le panier est vide
        cartRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        emptyCartLayout.visibility = if (isEmpty) View.VISIBLE else View.GONE
        cartFooter.visibility = if (isEmpty) View.GONE else View.VISIBLE

        // Mettre à jour le prix total
        if (!isEmpty) {
            totalPriceText.text = "€${"%.2f".format(CartManager.getTotalPrice())}"
        }

        // Mettre à jour les données de l'adapter
        cartAdapter.updateData(cartItems)
    }

    override fun onResume() {
        super.onResume()
        updateUI() // Rafraîchir l'UI au retour sur l'activité
    }
}