package com.example.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.databinding.ActivityProyectosBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProyectosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProyectosBinding
    private val lista = mutableListOf<Proyecto>()
    private lateinit var adapter: ProyectoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProyectosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ProyectoAdapter(lista)
        binding.rvProyectos.layoutManager = LinearLayoutManager(this)
        binding.rvProyectos.adapter = adapter

        cargarProyectos()
    }

    private fun cargarProyectos() {
        FirebaseDatabase.getInstance().reference.child("proyectos")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    for (hijo in snapshot.children) {
                        val proyecto = hijo.getValue(Proyecto::class.java)
                        if (proyecto != null) {
                            proyecto.id = hijo.key ?: ""
                            lista.add(proyecto)
                        }
                    }
                    // Los más recientes primero (push() genera llaves cronológicas)
                    lista.reverse()
                    adapter.notifyDataSetChanged()

                    if (lista.isEmpty()) {
                        binding.tvVacioProyectos.text = "Aún no hay proyectos publicados"
                        binding.tvVacioProyectos.visibility = View.VISIBLE
                    } else {
                        binding.tvVacioProyectos.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@ProyectosActivity,
                        "Error: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}