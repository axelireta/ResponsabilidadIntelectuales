package com.example.responsabilidad.data

// Objeto singleton: mantiene el estado del usuario mientras la app está abierta.
object EstadoUsuario {

    var autorizado: Boolean = false
    var solicitudEnviada: Boolean = false

    // Guarda las 5 respuestas de la prueba de aptitud
    var respuestasPrueba: MutableList<String> = mutableListOf()
}