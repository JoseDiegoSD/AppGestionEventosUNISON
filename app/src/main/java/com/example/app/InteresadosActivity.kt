package com.example.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.databinding.ActivityInteresadosBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InteresadosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInteresadosBinding
    private val lista = mutableListOf<Interesado>()
    private lateinit var adapter: InteresadoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInteresadosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = InteresadoAdapter(lista)
        binding.rvInteresados.layoutManager = LinearLayoutManager(this)
        binding.rvInteresados.adapter = adapter

        cargarInteresados()
    }

    private fun cargarInteresados() {
        FirebaseDatabase.getInstance().reference.child("interesados")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    for (hijo in snapshot.children) {
                        val interesado = hijo.getValue(Interesado::class.java)
                        if (interesado != null) {
                            interesado.id = hijo.key ?: ""
                            lista.add(interesado)
                        }
                    }
                    // Agrupa visualmente por evento
                    lista.sortBy { it.nombreEvento }
                    adapter.notifyDataSetChanged()

                    if (lista.isEmpty()) {
                        binding.tvVacioInteresados.text = "Aún no hay interesados registrados"
                        binding.tvVacioInteresados.visibility = View.VISIBLE
                    } else {
                        binding.tvVacioInteresados.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@InteresadosActivity,
                        "Error: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}