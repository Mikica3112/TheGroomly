package com.example.thegroomly

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thegroomly.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var b: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Prijava = samo validacija da nisu prazni
        b.btnLogin.setOnClickListener {
            val user = b.etUsername.text.toString().trim()
            val pass = b.etPassword.text.toString().trim()
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Unesite korisničko ime i lozinku", Toast.LENGTH_SHORT).show()
            } else {
                // Ovdje bi išla stvarna prijava; za sad prelazimo na MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        // Dummy Google i Facebook
        b.btnGoogle.setOnClickListener {
            Toast.makeText(this, "Google prijava (dummy)", Toast.LENGTH_SHORT).show()
        }
        b.btnFacebook.setOnClickListener {
            Toast.makeText(this, "Facebook prijava (dummy)", Toast.LENGTH_SHORT).show()
        }
    }
}
