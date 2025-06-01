package fr.epf.min1.projet_android_de_chassey_flouvat


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.epf.min1.projet_android_de_chassey_flouvat.data.User
import fr.epf.min1.projet_android_de_chassey_flouvat.repository.UserRepository

class RegisterActivity : BaseActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerButton: Button
    private val repository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_register)

        usernameInput = findViewById(R.id.register_username)
        emailInput = findViewById(R.id.register_email)
        passwordInput = findViewById(R.id.register_password)
        registerButton = findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                val user = User(username = username, email = email, password = password)
                repository.createUser(user).observe(this) { success ->
                    if (success!=null) {
                        Log.d("Debug_inscription", "utilisateur: $success")
                        Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Erreur d'inscription", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Tous les champs sont requis", Toast.LENGTH_SHORT).show()
            }
        }
    }
}