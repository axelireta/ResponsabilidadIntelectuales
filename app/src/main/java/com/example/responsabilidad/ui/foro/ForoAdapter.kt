package com.example.responsabilidad.ui.foro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.responsabilidad.data.Publicacion
import com.example.responsabilidad.databinding.ItemPublicacionBinding

class ForoAdapter : ListAdapter<Publicacion, ForoAdapter.PublicacionViewHolder>(DiffCallback()) {

    inner class PublicacionViewHolder(val binding: ItemPublicacionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacionViewHolder {
        val binding = ItemPublicacionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PublicacionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PublicacionViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvwTituloPublicacion.text = item.titulo
            tvwAutorPublicacion.text = "Publicado por: ${item.autor}"
            tvwContenidoPublicacion.text = item.contenido
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Publicacion>() {
        override fun areItemsTheSame(oldItem: Publicacion, newItem: Publicacion): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Publicacion, newItem: Publicacion): Boolean {
            return oldItem == newItem
        }
    }
}