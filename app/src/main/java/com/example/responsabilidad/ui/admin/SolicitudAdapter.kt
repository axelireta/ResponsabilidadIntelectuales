package com.example.responsabilidad.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.responsabilidad.databinding.ItemSolicitudBinding

class SolicitudAdapter(
    private val solicitudes: List<Map<String, Any>>,
    private val onAprobar: (String) -> Unit,
    private val onRechazar: (String) -> Unit
) : RecyclerView.Adapter<SolicitudAdapter.SolicitudViewHolder>() {

    inner class SolicitudViewHolder(val binding: ItemSolicitudBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudViewHolder {
        val binding = ItemSolicitudBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SolicitudViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SolicitudViewHolder, position: Int) {
        val solicitud = solicitudes[position]
        val docId = solicitud["docId"] as? String ?: ""
        val correo = solicitud["correo"] as? String ?: "Sin correo"

        @Suppress("UNCHECKED_CAST")
        val respuestas = solicitud["respuestas"] as? List<String> ?: emptyList()
        val resumen = if (respuestas.isNotEmpty()) "R1: ${respuestas[0].take(100)}..." else ""

        with(holder.binding) {
            tvwCorreoSolicitud.text = correo
            tvwResumenRespuestas.text = resumen
            btnAprobar.setOnClickListener { onAprobar(docId) }
            btnRechazar.setOnClickListener { onRechazar(docId) }
        }
    }

    override fun getItemCount() = solicitudes.size
}