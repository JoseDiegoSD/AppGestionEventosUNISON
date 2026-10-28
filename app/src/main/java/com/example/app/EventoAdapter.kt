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

    var interesadosPorEvento: Map<String, Int>? = null
    var errorInteresados: Boolean = false

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
            tvInteresados.text = interesadosPorEvento?.let {
                root.context.getString(R.string.interesados_cantidad, it[evento.id] ?: 0)
            } ?: root.context.getString(if (errorInteresados) R.string.interesados_error else R.string.interesados_cargando)
            ivEvento.visibility = if (evento.imagenUrl.isBlank()) android.view.View.GONE else android.view.View.VISIBLE

            Glide.with(root.context)
                .load(evento.imagenUrl)
                .centerCrop()
                .into(ivEvento)

            root.setOnClickListener { onClick(evento) }
        }
    }

    override fun getItemCount() = eventos.size
}