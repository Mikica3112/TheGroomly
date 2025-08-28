package com.example.thegroomly

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thegroomly.data.local.entities.ReservationEntity
import com.example.thegroomly.databinding.FragmentBookingBinding
import com.example.thegroomly.ui.booking.ReservationsAdapter
import java.util.Calendar

class BookingFragment : Fragment() {

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookingViewModel by viewModels()

    private val dogTypes = listOf("Mali pas", "Srednji pas", "Veliki pas")
    private val treatments = listOf("Kupanje", "Šišanje", "Njega šapa", "Full paket")

    // cache groomera kad stignu iz baze
    private var groomerIds: List<Long> = emptyList()
    private var groomerNames: List<String> = emptyList()

    private lateinit var adapter: ReservationsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Static spinneri
        binding.spDogType.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, dogTypes)
        binding.spTreatment.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, treatments)

        // Salon spinner (dinamički)
        binding.tvLoading.visibility = View.VISIBLE
        binding.spinnerGroomer.visibility = View.GONE

        viewModel.groomers.observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) {
                binding.tvLoading.visibility = View.VISIBLE
                binding.spinnerGroomer.visibility = View.GONE
            } else {
                groomerIds = list.map { it.id }
                groomerNames = list.map { it.name }

                binding.spinnerGroomer.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    groomerNames
                )

                binding.tvLoading.visibility = View.GONE
                binding.spinnerGroomer.visibility = View.VISIBLE
            }
        }

        // Date/Time picker
        binding.btnPickDateTime.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                TimePickerDialog(requireContext(), { _, h, min ->
                    val text = String.format("%02d.%02d.%04d %02d:%02d", d, m + 1, y, h, min)
                    binding.tvDateTime.text = text
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // RecyclerView
        adapter = ReservationsAdapter(emptyList()) { groomerId ->
            val idx = groomerIds.indexOf(groomerId)
            groomerNames.getOrNull(idx) ?: "Nepoznato"
        }
        binding.rvReservations.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReservations.adapter = adapter

        viewModel.reservations.observe(viewLifecycleOwner) { list ->
            adapter.submit(list)
            binding.tvEmpty.visibility = if (list.isNullOrEmpty()) View.VISIBLE else View.GONE
        }

        // Rezerviraj
        binding.btnReserve.setOnClickListener {
            val gIdx = binding.spinnerGroomer.selectedItemPosition
            if (gIdx == -1 || groomerIds.isEmpty()) {
                Toast.makeText(requireContext(), "Odaberi salon", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val groomerId = groomerIds[gIdx]
            val dogType = binding.spDogType.selectedItem as String
            val treatment = binding.spTreatment.selectedItem as String
            val dateTime = binding.tvDateTime.text?.toString()?.takeIf { it != "—" }
            if (dateTime.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Odaberi datum i vrijeme", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val note = binding.etNote.text?.toString()?.takeIf { it.isNotBlank() }

            val res = ReservationEntity(
                id = 0, // autoGenerate u entitetu
                groomerId = groomerId,
                dogType = dogType,
                treatment = treatment,
                dateTime = dateTime,
                note = note
            )
            viewModel.reserve(res)
            Toast.makeText(requireContext(), "Rezervacija spremljena!", Toast.LENGTH_SHORT).show()

            // reset (po želji)
            binding.tvDateTime.text = "—"
            binding.etNote.text?.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
