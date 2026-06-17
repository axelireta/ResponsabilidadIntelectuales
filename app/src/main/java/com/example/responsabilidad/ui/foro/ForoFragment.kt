package com.example.responsabilidad.ui.foro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.responsabilidad.R
import com.example.responsabilidad.data.EstadoUsuario
import com.example.responsabilidad.data.repositorio.FirestoreRepositorio
import com.example.responsabilidad.databinding.FragmentForoBinding
import androidx.navigation.fragment.findNavController

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

        binding.btnPostularme.setOnClickListener {
            findNavController().navigate(R.id.pruebaAptitudFragment)
        }

        cargarPublicaciones()
        verificarEstado()
    }

    override fun onResume() {
        super.onResume()
        verificarEstado()
    }

    private fun cargarPublicaciones() {
        FirestoreRepositorio.obtenerPublicaciones { lista ->
            adapter.submitList(lista)
        }
    }

    private fun verificarEstado() {
        FirestoreRepositorio.verificarEstadoUsuario { autorizado, pendiente ->
            activity?.runOnUiThread {
                actualizarVisibilidad(autorizado, pendiente)
            }
        }
    }

    private fun actualizarVisibilidad(autorizado: Boolean, pendiente: Boolean) {
        if (autorizado) {
            binding.fbtnPublicar.visibility = View.VISIBLE
            binding.cardPostularme.visibility = View.GONE
        } else {
            binding.fbtnPublicar.visibility = View.GONE
            binding.cardPostularme.visibility = View.VISIBLE
            if (pendiente) {
                binding.tvwMensajePostulacion.text = getString(R.string.mensaje_pendiente)
                binding.btnPostularme.isEnabled = false
            } else {
                binding.tvwMensajePostulacion.text = getString(R.string.mensaje_no_autorizado)
                binding.btnPostularme.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}