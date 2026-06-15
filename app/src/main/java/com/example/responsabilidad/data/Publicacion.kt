package com.example.responsabilidad.data

data class Publicacion(
    val id: Int,
    val autor: String,
    val titulo: String,
    val contenido: String,
    val estado: String // "Aprobado", "Pendiente", "Rechazado"
)