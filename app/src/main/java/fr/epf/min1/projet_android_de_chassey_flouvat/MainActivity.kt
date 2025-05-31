package fr.epf.min1.projet_android_de_chassey_flouvat

import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import androidx.lifecycle.Observer
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}