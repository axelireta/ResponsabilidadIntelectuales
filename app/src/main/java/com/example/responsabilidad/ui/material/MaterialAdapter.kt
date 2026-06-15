package com.example.responsabilidad.ui.material

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.responsabilidad.data.MaterialItem
import com.example.responsabilidad.databinding.ItemMaterialBinding

class MaterialAdapter : ListAdapter<MaterialItem, MaterialAdapter.MaterialViewHolder>(DiffCallback()) {

    inner class MaterialViewHolder(val binding: ItemMaterialBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val binding = ItemMaterialBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MaterialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvwTipoItem.text = item.tipo.uppercase()
            tvwTituloItem.text = item.titulo
            tvwAutorItem.text = "Por: ${item.autor}"
            tvwDescripcionItem.text = item.descripcion
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MaterialItem>() {
        override fun areItemsTheSame(oldItem: MaterialItem, newItem: MaterialItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MaterialItem, newItem: MaterialItem): Boolean {
            return oldItem == newItem
        }
    }
}