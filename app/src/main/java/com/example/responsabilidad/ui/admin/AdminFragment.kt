package com.example.responsabilidad.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.responsabilidad.R
import com.example.responsabilidad.data.repositorio.FirestoreRepositorio
import com.example.responsabilidad.databinding.FragmentAdminBinding

class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cargarEstadisticas()
        cargarSolicitudes()
    }

    private fun cargarEstadisticas() {
        FirestoreRepositorio.obtenerEstadisticas { stats ->
            activity?.runOnUiThread {
                binding.tvwTotalPublicaciones.text = getString(
                    R.string.stat_publicaciones, stats["publicaciones"] ?: 0
                )
                binding.tvwSolicitudesPendientes.text = getString(
                    R.string.stat_pendientes, stats["pendientes"] ?: 0
                )
                binding.tvwMiembrosAprobados.text = getString(
                    R.string.stat_aprobados, stats["aprobados"] ?: 0
                )
            }
        }
    }

    private fun cargarSolicitudes() {
        FirestoreRepositorio.obtenerSolicitudesPendientes { lista ->
            activity?.runOnUiThread {
                val adapter = SolicitudAdapter(
                    solicitudes = lista,
                    onAprobar = { docId ->
                        FirestoreRepositorio.aprobarSolicitud(docId) {
                            cargarSolicitudes()
                            cargarEstadisticas()
                        }
                    },
                    onRechazar = { docId ->
                        FirestoreRepositorio.rechazarSolicitud(docId) {
                            cargarSolicitudes()
                            cargarEstadisticas()
                        }
                    }
                )
                binding.rcvSolicitudes.layoutManager = LinearLayoutManager(requireContext())
                binding.rcvSolicitudes.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}