package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        cargarNombre()


        binding.btnEventos.setOnClickListener {
            startActivity(Intent(this, EventosActivity::class.java))
        }

        binding.btnPublicar.setOnClickListener {
            startActivity(Intent(this, PublicarProyectoActivity::class.java))
        }
        binding.btnProyectos.setOnClickListener {
            startActivity(Intent(this, ProyectosActivity::class.java))
        }

        binding.btnInteresados.setOnClickListener {
            startActivity(Intent(this, InteresadosActivity::class.java))
        }

        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun cargarNombre() {
        val uid = auth.currentUser?.uid ?: return
        FirebaseDatabase.getInstance().reference
            .child("usuarios").child(uid).child("nombre")
            .get()
            .addOnSuccessListener { binding.tvSaludo.text = "Hola, ${it.value ?: ""}" }
    }

    private fun proximamente(pantalla: String) =
        Toast.makeText(this, "$pantalla: pendiente", Toast.LENGTH_SHORT).show()
}