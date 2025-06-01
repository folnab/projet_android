package fr.epf.min1.projet_android_de_chassey_flouvat

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base_common)

        setupCommonUI()
    }

    fun setContentLayout(layoutResId: Int) {
        val contentContainer = findViewById<FrameLayout>(R.id.base_content_container)
        layoutInflater.inflate(layoutResId, contentContainer, true)
    }

    private fun setupCommonUI() {
        // FAB vers panier
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        val accountCard = findViewById<MaterialCardView>(R.id.main_Account_Card)

        accountCard.setOnClickListener { view ->
            val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
            val isLoggedIn = prefs.getBoolean("is_logged_in", false)

            val popup = PopupMenu(this, view)

            if (isLoggedIn) {
                popup.menuInflater.inflate(R.menu.account_popup_menu, popup.menu)
            } else {
                popup.menuInflater.inflate(R.menu.account_popup_menu_guest, popup.menu)
            }

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_profile -> {
                        Toast.makeText(this, "Profil", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ProfileActivity::class.java))
                        true
                    }
                    R.id.menu_logout -> {
                        Toast.makeText(this, "Déconnexion", Toast.LENGTH_SHORT).show()
                        prefs.edit().clear().apply()
                        true
                    }
                    R.id.menu_login -> {
                        startActivity(Intent(this, LoginActivity::class.java))
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }

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
                    val intent = Intent(this, QrScannerActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Account -> {
                    val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
                    val isLoggedIn = prefs.getBoolean("is_logged_in", false)

                    if (!isLoggedIn) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        startActivity(Intent(this, ProfileActivity::class.java))
                        Toast.makeText(this, "Profil", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }
    }
}