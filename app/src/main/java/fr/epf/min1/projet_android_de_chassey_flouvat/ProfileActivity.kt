package fr.epf.min1.projet_android_de_chassey_flouvat

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileActivity : BaseActivity() {

    override fun getBottomNavItemId(): Int = R.id.Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_profile)

        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val username = prefs.getString("username", "Nom d’utilisateur inconnu")
        val email = prefs.getString("email", "Email inconnu")

        val usernameText = findViewById<TextView>(R.id.profile_username)
        val emailText = findViewById<TextView>(R.id.profile_email)

        usernameText.text = "Nom d'utilisateur : $username"
        emailText.text = "Email : $email"

        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}
