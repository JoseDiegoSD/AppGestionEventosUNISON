package com.example.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app.databinding.ItemEventoBinding

class EventoAdapter(
    private val eventos: List<Evento>,
    private val onClick: (Evento) -> Unit
) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    inner class EventoViewHolder(val binding: ItemEventoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val binding = ItemEventoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EventoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventos[position]
        with(holder.binding) {
            tvNombre.text = evento.nombre
            tvFechaHora.text = evento.fechaHora
            tvLugar.text = evento.lugar

            Glide.with(root.context)
                .load(evento.imagenUrl)
                .centerCrop()
                .into(ivEvento)

            root.setOnClickListener { onClick(evento) }
        }
    }

    override fun getItemCount() = eventos.size
}