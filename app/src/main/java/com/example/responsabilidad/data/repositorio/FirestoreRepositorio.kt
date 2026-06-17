package com.example.responsabilidad.data.repositorio

import com.example.responsabilidad.data.EstadoUsuario
import com.example.responsabilidad.data.Publicacion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object FirestoreRepositorio {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Colecciones en Firestore
    private const val COLECCION_PUBLICACIONES = "Publicaciones"
    private const val COLECCION_SOLICITUDES = "solicitudes"
    private const val COLECCION_USUARIOS = "usuarios"

    // UID del administrador (se puede cambiar después)
    private const val UID_ADMIN = "rdSnsTWCFXVASeg2b2hOqkEu6se2"

    fun esAdmin(): Boolean {
        return auth.currentUser?.uid == UID_ADMIN
    }

    fun obtenerPublicaciones(callback: (List<Publicacion>) -> Unit) {
        db.collection(COLECCION_PUBLICACIONES)
            .whereEqualTo("estado", "Aprobado")
            .orderBy("fechaCreacion", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { resultado ->
                val lista = resultado.documents.mapNotNull { doc ->
                    Publicacion(
                        id = doc.id.hashCode(),
                        autor = doc.getString("autor") ?: "Anónimo",
                        titulo = doc.getString("titulo") ?: "",
                        contenido = doc.getString("contenido") ?: "",
                        estado = doc.getString("estado") ?: "Aprobado"
                    )
                }
                callback(lista)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun guardarPublicacion(titulo: String, contenido: String, onExito: () -> Unit, onError: (String) -> Unit) {
        val usuario = auth.currentUser ?: return
        val publicacion = hashMapOf(
            "autor" to (usuario.email ?: "Miembro"),
            "titulo" to titulo,
            "contenido" to contenido,
            "estado" to "Pendiente",
            "fechaCreacion" to com.google.firebase.Timestamp.now()
        )
        db.collection(COLECCION_PUBLICACIONES)
            .add(publicacion)
            .addOnSuccessListener { onExito() }
            .addOnFailureListener { e -> onError(e.message ?: "Error al publicar") }
    }

    fun enviarSolicitudAptitud(respuestas: List<String>, onExito: () -> Unit, onError: (String) -> Unit) {
        val usuario = auth.currentUser ?: return
        val solicitud = hashMapOf(
            "uid" to usuario.uid,
            "correo" to (usuario.email ?: ""),
            "respuestas" to respuestas,
            "estado" to "Pendiente",
            "fechaEnvio" to com.google.firebase.Timestamp.now()
        )
        db.collection(COLECCION_SOLICITUDES)
            .document(usuario.uid)
            .set(solicitud)
            .addOnSuccessListener {
                EstadoUsuario.solicitudEnviada = true
                onExito()
            }
            .addOnFailureListener { e -> onError(e.message ?: "Error al enviar") }
    }

    fun verificarEstadoUsuario(onResultado: (autorizado: Boolean, pendiente: Boolean) -> Unit) {
        val usuario = auth.currentUser ?: run {
            onResultado(false, false)
            return
        }
        db.collection(COLECCION_SOLICITUDES)
            .document(usuario.uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val estado = doc.getString("estado") ?: "Pendiente"
                    val autorizado = estado == "Aprobado"
                    val pendiente = estado == "Pendiente"
                    EstadoUsuario.autorizado = autorizado
                    EstadoUsuario.solicitudEnviada = pendiente || autorizado
                    onResultado(autorizado, pendiente)
                } else {
                    onResultado(false, false)
                }
            }
            .addOnFailureListener { onResultado(false, false) }
    }

    fun obtenerSolicitudesPendientes(callback: (List<Map<String, Any>>) -> Unit) {
        db.collection(COLECCION_SOLICITUDES)
            .whereEqualTo("estado", "Pendiente")
            .get()
            .addOnSuccessListener { resultado ->
                val lista = resultado.documents.mapNotNull { doc ->
                    doc.data?.toMutableMap()?.also { it["docId"] = doc.id }
                }
                callback(lista)
            }
            .addOnFailureListener { callback(emptyList()) }
    }

    fun aprobarSolicitud(docId: String, onExito: () -> Unit) {
        db.collection(COLECCION_SOLICITUDES)
            .document(docId)
            .update("estado", "Aprobado")
            .addOnSuccessListener { onExito() }
    }

    fun rechazarSolicitud(docId: String, onExito: () -> Unit) {
        db.collection(COLECCION_SOLICITUDES)
            .document(docId)
            .update("estado", "Rechazado")
            .addOnSuccessListener { onExito() }
    }

    fun obtenerEstadisticas(callback: (Map<String, Int>) -> Unit) {
        val stats = mutableMapOf("publicaciones" to 0, "pendientes" to 0, "aprobados" to 0)
        db.collection(COLECCION_PUBLICACIONES).get()
            .addOnSuccessListener { pub ->
                stats["publicaciones"] = pub.size()
                db.collection(COLECCION_SOLICITUDES).get()
                    .addOnSuccessListener { sol ->
                        stats["pendientes"] = sol.documents.count { it.getString("estado") == "Pendiente" }
                        stats["aprobados"] = sol.documents.count { it.getString("estado") == "Aprobado" }
                        callback(stats)
                    }
            }
    }
}