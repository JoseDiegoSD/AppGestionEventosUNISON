package com.example.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.ItemProyectoBinding

class ProyectoAdapter(
    private val proyectos: List<Proyecto>
) : RecyclerView.Adapter<ProyectoAdapter.ProyectoViewHolder>() {

    inner class ProyectoViewHolder(val binding: ItemProyectoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProyectoViewHolder {
        val binding = ItemProyectoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProyectoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProyectoViewHolder, position: Int) {
        val p = proyectos[position]
        with(holder.binding) {
            tvProyNombre.text = p.nombre
            tvProyDescripcion.text = p.descripcion
            tvProyResponsables.text = "Responsables: ${p.responsables}"
            tvProyFecha.text = "Publicado: ${p.fechaPublicacion}"
        }
    }

    override fun getItemCount() = proyectos.size
}