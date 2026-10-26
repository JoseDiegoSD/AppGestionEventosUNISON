package com.example.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityPublicarProyectoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PublicarProyectoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublicarProyectoBinding
    private val db = FirebaseDatabase.getInstance().reference

    private val fecha: String = SimpleDateFormat("dd/MM/yyyy", Locale("es", "MX"))
        .format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublicarProyectoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvFechaPublicacion.text = "Fecha de publicación: $fecha"

        binding.btnPublicar.setOnClickListener { publicar() }
    }

    private fun publicar() {
        val nombre = binding.etNombreProyecto.text.toString().trim()
        val descripcion = binding.etDescripcionProyecto.text.toString().trim()
        val responsables = binding.etResponsables.text.toString().trim()

        if (nombre.isEmpty() || descripcion.isEmpty() || responsables.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val id = db.child("proyectos").push().key ?: return

        val proyecto = mapOf(
            "nombre" to nombre,
            "descripcion" to descripcion,
            "responsables" to responsables,
            "fechaPublicacion" to fecha,
            "autorId" to uid
        )

        binding.btnPublicar.isEnabled = false

        db.child("proyectos").child(id).setValue(proyecto)
            .addOnSuccessListener {
                Toast.makeText(this, "Proyecto publicado", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                binding.btnPublicar.isEnabled = true
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}