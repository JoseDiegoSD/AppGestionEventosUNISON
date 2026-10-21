package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Si ya hay sesión iniciada, pasa directo al menú
        if (auth.currentUser != null) {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Botón de inicio de sesión
        binding.btnIniciarSesion.setOnClickListener {

            val correo = binding.etCorreo.text.toString().trim()
            val contrasena = binding.etContrasena.text.toString()

            if (correo.isEmpty() || contrasena.isEmpty()) {

                Toast.makeText(
                    this,
                    "Ingresa tu correo y contraseña",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                binding.btnIniciarSesion.isEnabled = false

                auth.signInWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(this) { task ->

                        binding.btnIniciarSesion.isEnabled = true

                        if (task.isSuccessful) {

                            startActivity(Intent(this, MenuActivity::class.java))
                            finish()

                        } else {

                            Toast.makeText(
                                this,
                                task.exception?.localizedMessage
                                    ?: "No se pudo iniciar sesión",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }

        // Enlace para registrarse
        binding.tvRegistrarse.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}