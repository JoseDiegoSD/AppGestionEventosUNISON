package com.example.app

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnCrearCuenta.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val correo = binding.etCorreoRegistro.text.toString().trim()
            val contrasena = binding.etContrasenaRegistro.text.toString()
            val confirmar = binding.etConfirmar.text.toString()

            when {
                nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() ->
                    mostrar("Completa todos los campos")
                !Patterns.EMAIL_ADDRESS.matcher(correo).matches() ->
                    mostrar("Correo no válido")
                contrasena.length < 6 ->
                    mostrar("La contraseña debe tener al menos 6 caracteres")
                contrasena != confirmar ->
                    mostrar("Las contraseñas no coinciden")
                else -> crearCuenta(nombre, correo, contrasena)
            }
        }

        binding.tvVolverLogin.setOnClickListener { finish() }
    }

    private fun crearCuenta(nombre: String, correo: String, contrasena: String) {
        binding.btnCrearCuenta.isEnabled = false

        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser!!.uid
                    val datos = mapOf("nombre" to nombre, "correo" to correo)

                    FirebaseDatabase.getInstance().reference
                        .child("usuarios").child(uid)
                        .setValue(datos)
                        .addOnCompleteListener {
                            mostrar("Cuenta creada")
                            val intent = Intent(this, MenuActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                } else {
                    binding.btnCrearCuenta.isEnabled = true
                    mostrar(task.exception?.localizedMessage ?: "No se pudo crear la cuenta")
                }
            }
    }

    private fun mostrar(mensaje: String) =
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
}