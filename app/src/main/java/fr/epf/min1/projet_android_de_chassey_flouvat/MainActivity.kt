package fr.epf.min1.projet_android_de_chassey_flouvat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.PopupMenu
import android.widget.Toast
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import fr.epf.min1.projet_android_de_chassey_flouvat.data.Product
import fr.epf.min1.projet_android_de_chassey_flouvat.repository.ProductRepository
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.epf.min1.projet_android_de_chassey_flouvat.data.CartManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        val accountCard = findViewById<MaterialCardView>(R.id.main_Account_Card)

        val searchEditText = findViewById<AutoCompleteTextView>(R.id.search_edit_text)
        val searchButton = findViewById<CardView>(R.id.search_button)
        var allProduits: List<Product> = emptyList()

        accountCard.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.account_popup_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_profile -> {
                        Toast.makeText(this, "Profil cliqué", Toast.LENGTH_SHORT).show()
                        // Ex: startActivity(Intent(this, ProfileActivity::class.java))
                        true
                    }
                    R.id.menu_logout -> {
                        Toast.makeText(this, "Déconnexion", Toast.LENGTH_SHORT).show()
                        // Ex: logique de déconnexion
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }

        val container = findViewById<LinearLayout>(R.id.main_product_container)
        val repository = ProductRepository()
        repository.getAllProducts().observe(this) { produits ->
            if (!produits.isNullOrEmpty()) {
                allProduits=produits

                val nomsProduits = allProduits.map { it.title }
                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nomsProduits)
                searchEditText.setAdapter(adapter)

                val top10 = produits.take(10)

                val categories = produits.map { it.category }.distinct()
                val categoriesContainer = findViewById<LinearLayout>(R.id.categories_container)

                for (categorie in categories) {
                    val card = LayoutInflater.from(this).inflate(R.layout.item_category_card, categoriesContainer, false) as CardView

                    val textView = card.findViewById<TextView>(R.id.category_name)
                    textView.text = categorie

                    card.setOnClickListener {
                        val produitsFiltres = produits.filter { it.category == categorie }
                        AfficherProduits(produitsFiltres, container)
                    }

                    categoriesContainer.addView(card)
                }
                AfficherProduits(produits.take(10), container)

                searchButton.setOnClickListener {
                    searchEditText?.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                    val query = searchEditText.text.toString().trim()
                    val resultats = allProduits.filter { it.title.contains(query, ignoreCase = true) }
                    AfficherProduits(resultats, container)
                }
                searchEditText.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
                        container.removeAllViews()
                    }
                }
                searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val query = s.toString().trim()
                        val resultats = allProduits.filter { it.title.contains(query, ignoreCase = true) }
                        AfficherProduits(resultats, container)
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })
            }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> {
                    if (this !is MainActivity) startActivity(Intent(this, MainActivity::class.java))

                    searchEditText?.clearFocus()

                    // 2. Cacher le clavier s'il est ouvert
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)

                    // 3. Réafficher les 10 premiers produits
                    AfficherProduits(allProduits.take(10), findViewById(R.id.main_product_container))

                    true
                }
                R.id.Search -> {
                    searchEditText?.requestFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun remplirProduitDansCard(view: View, produit: Product) {
        val imageView = view.findViewById<ImageView>(R.id.product_image)
        val prixText = view.findViewById<TextView>(R.id.product_price)
        val cardView = view.findViewById<CardView>(R.id.product_card)
        val addPanierView = view.findViewById<CardView>(R.id.ajout_au_panier)

        prixText.text = "${produit.price} €"

        Glide.with(view)
            .load(produit.image)
            .into(imageView)

        // Attache le produit à la vue (via tag)
        cardView.tag = produit

        cardView.setOnClickListener{
            val intent = Intent(view.context, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT_ID", produit.id)
            view.context.startActivity(intent)
        }
        addPanierView.setOnClickListener {
            CartManager.addProduct(produit)
            Toast.makeText(view.context, "${produit.title} ajouté au panier", Toast.LENGTH_SHORT).show()
        }
    }

    fun AfficherProduits(produits : List<Product>, container : LinearLayout){
        container.removeAllViews()
        for (i in 0 until produits.size step 2) {
            val rowView = LayoutInflater.from(this).inflate(R.layout.item_product_row, container, false) as LinearLayout

            val card1 = rowView.getChildAt(0)
            val card2 = rowView.getChildAt(1)

            remplirProduitDansCard(card1, produits[i])

            if (i + 1 < produits.size) {
                remplirProduitDansCard(card2, produits[i + 1])
            } else {
                card2.visibility = View.INVISIBLE
            }

            container.addView(rowView)
        }
    }
}