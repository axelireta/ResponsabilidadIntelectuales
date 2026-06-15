package com.example.responsabilidad.ui.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.responsabilidad.data.MaterialItem
import com.example.responsabilidad.databinding.FragmentMaterialBinding

class MaterialFragment : Fragment() {

    private var _binding: FragmentMaterialBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MaterialAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaterialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MaterialAdapter()
        binding.rcvMaterial.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvMaterial.adapter = adapter

        adapter.submitList(obtenerMaterialDePrueba())
    }

    private fun obtenerMaterialDePrueba(): List<MaterialItem> {
        return listOf(
            MaterialItem(
                id = 1,
                titulo = "Sobre la responsabilidad del pensamiento crítico",
                autor = "UNAM - Facultad de Filosofía y Letras",
                tipo = "Ensayo",
                descripcion = "Una reflexión sobre el papel de los pensadores en la sociedad contemporánea y su deber ético frente a la verdad."
            ),
            MaterialItem(
                id = 2,
                titulo = "El rol del intelectual en la era digital",
                autor = "Instituto de Investigaciones Sociales",
                tipo = "Video",
                descripcion = "Conferencia sobre cómo la información masiva transforma la manera en que los pensadores influyen en la opinión pública."
            ),
            MaterialItem(
                id = 3,
                titulo = "Pensar en comunidad: hacia un nuevo contrato social",
                autor = "Red de Universidades Latinoamericanas",
                tipo = "Idea",
                descripcion = "Propuesta breve sobre cómo construir espacios de diálogo entre ciudadanos y academia."
            ),
            MaterialItem(
                id = 4,
                titulo = "Ética y verdad en tiempos de desinformación",
                autor = "Centro de Estudios Filosóficos",
                tipo = "Ensayo",
                descripcion = "Análisis de los desafíos éticos que enfrentan los intelectuales al comunicar ideas en plataformas digitales."
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}