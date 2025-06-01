package fr.epf.min1.projet_android_de_chassey_flouvat

import android.content.Intent
import android.os.Bundle
import android.widget.*
import fr.epf.min1.projet_android_de_chassey_flouvat.repository.AuthRepository
import fr.epf.min1.projet_android_de_chassey_flouvat.repository.UserRepository

class LoginActivity : BaseActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView

    private val authRepo = AuthRepository()

    override fun getBottomNavItemId(): Int = R.id.Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_login)

        usernameEditText = findViewById(R.id.username_input)
        passwordEditText = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)
        registerLink = findViewById(R.id.register_link)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                authRepo.login(username, password).observe(this) { token ->
                    if (token != null) {
                        // Retrouver l'utilisateur complet pour obtenir l'id
                        val userRepo = UserRepository()
                        userRepo.getUserByUsername(username).observe(this) { user ->
                            if (user != null) {
                                val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
                                prefs.edit()
                                    .putBoolean("is_logged_in", true)
                                    .putInt("user_id", user.id)
                                    .putString("username", user.username)
                                    .putString("email", user.email)
                                    .apply()

                                Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Utilisateur introuvable", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Identifiants incorrects", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Champs requis", Toast.LENGTH_SHORT).show()
            }
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}