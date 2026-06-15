package com.example.responsabilidad.ui.foro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.responsabilidad.data.EstadoUsuario
import com.example.responsabilidad.data.Publicacion
import com.example.responsabilidad.databinding.FragmentForoBinding
import com.example.responsabilidad.R

class ForoFragment : Fragment() {

    private var _binding: FragmentForoBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ForoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ForoAdapter()
        binding.rcvForo.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvForo.adapter = adapter
        adapter.submitList(obtenerPublicacionesDePrueba())

        binding.btnPostularme.setOnClickListener {
            findNavController().navigate(R.id.pruebaAptitudFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarVisibilidadSegunEstado()
    }

    private fun actualizarVisibilidadSegunEstado() {
        if (EstadoUsuario.autorizado) {
            binding.fbtnPublicar.visibility = View.VISIBLE
            binding.cardPostularme.visibility = View.GONE
        } else {
            binding.fbtnPublicar.visibility = View.GONE
            binding.cardPostularme.visibility = View.VISIBLE

            if (EstadoUsuario.solicitudEnviada) {
                binding.tvwMensajePostulacion.text = getString(R.string.mensaje_pendiente)
                binding.btnPostularme.isEnabled = false
            } else {
                binding.tvwMensajePostulacion.text = getString(R.string.mensaje_no_autorizado)
                binding.btnPostularme.isEnabled = true
            }
        }
    }

    private fun obtenerPublicacionesDePrueba(): List<Publicacion> {
        return listOf(
            Publicacion(
                id = 1,
                autor = "Miembro 0231",
                titulo = "¿Es posible la objetividad total en el periodismo?",
                contenido = "Considero que la objetividad absoluta es un ideal inalcanzable, pero eso no significa que no debamos esforzarnos por acercarnos a ella mediante el contraste de fuentes y la transparencia metodológica.",
                estado = "Aprobado"
            ),
            Publicacion(
                id = 2,
                autor = "Miembro 0117",
                titulo = "Educación y pensamiento crítico en la era de la IA",
                contenido = "Si delegamos cada vez más nuestras decisiones a sistemas automatizados, ¿qué papel le queda al juicio humano? Propongo que la educación debe enfocarse en formular mejores preguntas, no solo en memorizar respuestas.",
                estado = "Aprobado"
            ),
            Publicacion(
                id = 3,
                autor = "Miembro 0089",
                titulo = "El debate público como herramienta de cohesión social",
                contenido = "Los espacios de discusión abierta, cuando se conducen con respeto, permiten que comunidades con visiones distintas encuentren puntos en común sin necesidad de imponer una sola verdad.",
                estado = "Aprobado"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}