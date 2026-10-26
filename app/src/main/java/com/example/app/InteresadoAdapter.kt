package com.example.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.ItemInteresadoBinding

class InteresadoAdapter(
    private val interesados: List<Interesado>
) : RecyclerView.Adapter<InteresadoAdapter.InteresadoViewHolder>() {

    inner class InteresadoViewHolder(val binding: ItemInteresadoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InteresadoViewHolder {
        val binding = ItemInteresadoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return InteresadoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InteresadoViewHolder, position: Int) {
        val i = interesados[position]
        with(holder.binding) {
            tvIntNombre.text = i.nombreUsuario
            tvIntCorreo.text = i.correoUsuario
            tvIntEvento.text = "Evento: ${i.nombreEvento}"
            tvIntFecha.text = "Registrado: ${i.fechaRegistro}"
        }
    }

    override fun getItemCount() = interesados.size
}