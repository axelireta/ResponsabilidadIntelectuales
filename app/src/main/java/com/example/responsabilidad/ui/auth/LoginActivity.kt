package com.example.responsabilidad.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.responsabilidad.MainActivity
import com.example.responsabilidad.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.responsabilidad.R

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Si ya hay sesión iniciada, ir directo a MainActivity
        if (auth.currentUser != null) {
            irAMainActivity()
            return
        }

        binding.btnIniciarSesion.setOnClickListener {
            iniciarSesion()
        }

        binding.btnRegistrarse.setOnClickListener {
            registrarse()
        }
    }

    private fun iniciarSesion() {
        val correo = binding.etCorreo.text.toString().trim()
        val contrasena = binding.etContrasena.text.toString().trim()

        if (correo.isBlank() || contrasena.isBlank()) {
            mostrarError(getString(R.string.error_campos_vacios))
            return
        }

        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnSuccessListener {
                irAMainActivity()
            }
            .addOnFailureListener { excepcion ->
                mostrarError(excepcion.message ?: getString(R.string.error_login))
            }
    }

    private fun registrarse() {
        val correo = binding.etCorreo.text.toString().trim()
        val contrasena = binding.etContrasena.text.toString().trim()

        if (correo.isBlank() || contrasena.isBlank()) {
            mostrarError(getString(R.string.error_campos_vacios))
            return
        }

        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnSuccessListener {
                irAMainActivity()
            }
            .addOnFailureListener { excepcion ->
                mostrarError(excepcion.message ?: getString(R.string.error_registro))
            }
    }

    private fun mostrarError(mensaje: String) {
        binding.tvwError.visibility = View.VISIBLE
        binding.tvwError.text = mensaje
    }

    private fun irAMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}