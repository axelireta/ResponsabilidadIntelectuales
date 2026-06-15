package com.example.responsabilidad.data

data class MaterialItem(
    val id: Int,
    val titulo: String,
    val autor: String,
    val tipo: String, // "Ensayo", "Video", "Idea"
    val descripcion: String
)