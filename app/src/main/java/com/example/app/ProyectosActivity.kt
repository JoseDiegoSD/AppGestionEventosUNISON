package com.example.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.databinding.ActivityProyectosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Query
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProyectosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProyectosBinding
    private val lista = mutableListOf<Proyecto>()
    private lateinit var adapter: ProyectoAdapter
    private var proyectosQuery: Query? = null
    private var proyectosListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProyectosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ProyectoAdapter(lista)
        binding.rvProyectos.layoutManager = LinearLayoutManager(this)
        binding.rvProyectos.adapter = adapter

        cargarProyectos()
    }

    override fun onDestroy() {
        proyectosListener?.let { proyectosQuery?.removeEventListener(it) }
        super.onDestroy()
    }

    private fun cargarProyectos() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            binding.tvVacioProyectos.text = "Inicia sesión para ver tus proyectos"
            binding.tvVacioProyectos.visibility = View.VISIBLE
            return
        }
        val query = FirebaseDatabase.getInstance().reference.child("proyectos")
            .orderByChild("autorId").equalTo(uid)
        proyectosQuery = query
        proyectosListener = query.addValueEventListener(object : ValueEventListener {
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
                        binding.tvVacioProyectos.text = "Aún no has publicado proyectos"
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