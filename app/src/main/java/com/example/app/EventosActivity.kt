package com.example.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.databinding.ActivityEventosBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EventosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventosBinding
    private val listaEventos = mutableListOf<Evento>()
    private lateinit var adapter: EventoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = EventoAdapter(listaEventos) { evento ->
            // Aquí abriremos el detalle del evento en el siguiente paso
            Toast.makeText(this, "Seleccionaste: ${evento.nombre}", Toast.LENGTH_SHORT).show()
        }

        binding.rvEventos.layoutManager = LinearLayoutManager(this)
        binding.rvEventos.adapter = adapter

        cargarEventos()
    }

    private fun cargarEventos() {
        FirebaseDatabase.getInstance().reference.child("eventos")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listaEventos.clear()
                    for (hijo in snapshot.children) {
                        val evento = hijo.getValue(Evento::class.java)
                        if (evento != null) {
                            evento.id = hijo.key ?: ""
                            listaEventos.add(evento)
                        }
                    }
                    adapter.notifyDataSetChanged()

                    if (listaEventos.isEmpty()) {
                        binding.tvVacio.text = "No hay eventos por el momento"
                        binding.tvVacio.visibility = View.VISIBLE
                    } else {
                        binding.tvVacio.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@EventosActivity,
                        "Error: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}