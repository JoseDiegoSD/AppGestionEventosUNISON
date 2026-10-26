package com.example.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.app.databinding.ActivityDetalleEventoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetalleEventoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleEventoBinding
    private val db = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    private var eventoId = ""
    private var nombreEvento = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventoId = intent.getStringExtra("id") ?: ""
        nombreEvento = intent.getStringExtra("nombre") ?: ""

        binding.tvDetalleNombre.text = nombreEvento
        binding.tvDetalleFecha.text = intent.getStringExtra("fechaHora")
        binding.tvDetalleLugar.text = intent.getStringExtra("lugar")
        binding.tvDetalleDescripcion.text = intent.getStringExtra("descripcion")

        Glide.with(this)
            .load(intent.getStringExtra("imagenUrl"))
            .centerCrop()
            .into(binding.ivDetalle)

        verificarRegistro()

        binding.btnMeInteresa.setOnClickListener { registrarInteres() }
    }

    // Llave única: un usuario solo puede registrarse una vez por evento
    private fun llaveRegistro(): String? {
        val uid = auth.currentUser?.uid ?: return null
        return "${eventoId}_$uid"
    }

    private fun verificarRegistro() {
        val llave = llaveRegistro() ?: return
        db.child("interesados").child(llave).get()
            .addOnSuccessListener { if (it.exists()) marcarRegistrado() }
    }

    private fun registrarInteres() {
        val uid = auth.currentUser?.uid
        val llave = llaveRegistro()
        if (uid == null || llave == null) {
            Toast.makeText(this, "Inicia sesión primero", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnMeInteresa.isEnabled = false

        // Primero obtenemos el nombre del usuario
        db.child("usuarios").child(uid).child("nombre").get()
            .addOnSuccessListener { snap ->
                val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "MX"))
                    .format(Date())

                val registro = mapOf(
                    "usuarioId" to uid,
                    "nombreUsuario" to (snap.value?.toString() ?: ""),
                    "correoUsuario" to (auth.currentUser?.email ?: ""),
                    "eventoId" to eventoId,
                    "nombreEvento" to nombreEvento,
                    "fechaRegistro" to fecha
                )

                db.child("interesados").child(llave).setValue(registro)
                    .addOnSuccessListener {
                        Toast.makeText(this, "¡Registro guardado!", Toast.LENGTH_SHORT).show()
                        marcarRegistrado()
                    }
                    .addOnFailureListener {
                        binding.btnMeInteresa.isEnabled = true
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener {
                binding.btnMeInteresa.isEnabled = true
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun marcarRegistrado() {
        binding.btnMeInteresa.text = "Ya registraste tu interés"
        binding.btnMeInteresa.isEnabled = false
    }
}