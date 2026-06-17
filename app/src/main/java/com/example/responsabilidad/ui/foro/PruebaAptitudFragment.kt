package com.example.responsabilidad.ui.foro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.responsabilidad.R
import com.example.responsabilidad.data.repositorio.FirestoreRepositorio
import com.example.responsabilidad.databinding.FragmentPruebaAptitudBinding

class PruebaAptitudFragment : Fragment() {

    private var _binding: FragmentPruebaAptitudBinding? = null
    private val binding get() = _binding!!

    private val minimoPalabrasPorRespuesta = 15

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPruebaAptitudBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEnviarPrueba.setOnClickListener {
            procesarEnvio()
        }
    }

    private fun procesarEnvio() {
        val respuestas = listOf(
            binding.etRespuesta1.text.toString().trim(),
            binding.etRespuesta2.text.toString().trim(),
            binding.etRespuesta3.text.toString().trim(),
            binding.etRespuesta4.text.toString().trim(),
            binding.etRespuesta5.text.toString().trim()
        )

        val respuestasValidas = respuestas.all { contarPalabras(it) >= minimoPalabrasPorRespuesta }

        if (!respuestasValidas) {
            binding.tvwErrorValidacion.visibility = View.VISIBLE
            binding.tvwErrorValidacion.text = getString(
                R.string.error_respuestas_cortas,
                minimoPalabrasPorRespuesta
            )
            return
        }

        binding.tvwErrorValidacion.visibility = View.GONE
        binding.btnEnviarPrueba.isEnabled = false

        FirestoreRepositorio.enviarSolicitudAptitud(
            respuestas = respuestas,
            onExito = {
                activity?.runOnUiThread {
                    findNavController().popBackStack()
                }
            },
            onError = { mensaje ->
                activity?.runOnUiThread {
                    binding.tvwErrorValidacion.visibility = View.VISIBLE
                    binding.tvwErrorValidacion.text = mensaje
                    binding.btnEnviarPrueba.isEnabled = true
                }
            }
        )
    }

    private fun contarPalabras(texto: String): Int {
        if (texto.isBlank()) return 0
        return texto.split("\\s+".toRegex()).filter { it.isNotBlank() }.size
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}