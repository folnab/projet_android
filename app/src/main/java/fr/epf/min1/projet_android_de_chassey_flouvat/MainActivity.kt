package fr.epf.min1.projet_android_de_chassey_flouvat

import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val accountCard = findViewById<MaterialCardView>(R.id.main_Account_Card)

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
                val top10 = produits.take(10)

                val categories = produits.map { it.category }.distinct()
                val categoriesContainer = findViewById<LinearLayout>(R.id.categories_container)

                for (categorie in categories) {
                    val card = LayoutInflater.from(this).inflate(R.layout.item_category_card, categoriesContainer, false) as CardView

                    val textView = card.findViewById<TextView>(R.id.category_name)
                    textView.text = categorie

                    card.setOnClickListener {
                        Toast.makeText(this, "Catégorie : $categorie", Toast.LENGTH_SHORT).show()
                        // ➕ Bonus : tu peux ici filtrer les produits
                    }

                    categoriesContainer.addView(card)
                }
                for (i in 0 until top10.size step 2) {
                    val rowView = LayoutInflater.from(this).inflate(R.layout.item_product_row, container, false) as LinearLayout

                    val card1 = rowView.getChildAt(0)
                    val card2 = rowView.getChildAt(1)

                    remplirProduitDansCard(card1, top10[i])

                    if (i + 1 < top10.size) {
                        remplirProduitDansCard(card2, top10[i + 1])
                    } else {
                        card2.visibility = View.INVISIBLE
                    }

                    container.addView(rowView)
                }
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

        prixText.text = "${produit.price} €"

        Glide.with(view)
            .load(produit.image)
            .into(imageView)

        // Attache le produit à la vue (via tag)
        cardView.tag = produit
    }
}