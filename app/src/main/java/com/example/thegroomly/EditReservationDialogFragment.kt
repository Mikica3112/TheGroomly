package com.example.thegroomly.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.thegroomly.BookingViewModel
import com.example.thegroomly.data.local.entities.ReservationEntity
import com.example.thegroomly.databinding.DialogEditReservationBinding
import java.util.Calendar

class EditReservationDialogFragment : DialogFragment() {

    private var _binding: DialogEditReservationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookingViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogEditReservationBinding.inflate(LayoutInflater.from(requireContext()))

        // args
        val id = requireArguments().getLong(ARG_ID)
        val groomerId = requireArguments().getLong(ARG_GROOMER_ID)
        val dogTypeArg = requireArguments().getString(ARG_DOG_TYPE) ?: "Mali pas"
        val treatmentArg = requireArguments().getString(ARG_TREATMENT) ?: "Kupanje"
        val dateTimeArg = requireArguments().getString(ARG_DATETIME) ?: "—"
        val noteArg = requireArguments().getString(ARG_NOTE)

        // spinners
        val dogTypes = listOf("Mali pas", "Srednji pas", "Veliki pas")
        binding.spDogType.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, dogTypes)
        binding.spDogType.setSelection(dogTypes.indexOf(dogTypeArg).coerceAtLeast(0))

        val treatments = listOf("Kupanje", "Šišanje", "Njega šapa", "Full paket")
        binding.spTreatment.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, treatments)
        binding.spTreatment.setSelection(treatments.indexOf(treatmentArg).coerceAtLeast(0))

        binding.tvDateTime.text = dateTimeArg
        binding.etNote.setText(noteArg ?: "")

        binding.btnPick.setOnClickListener {
            val cal = Calendar.getInstance()
            val y = cal.get(Calendar.YEAR)
            val m = cal.get(Calendar.MONTH)
            val d = cal.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(), { _, yy, mm, dd ->
                TimePickerDialog(requireContext(), { _, h, min ->
                    binding.tvDateTime.text = String.format("%02d.%02d.%04d %02d:%02d", dd, mm+1, yy, h, min)
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }, y, m, d).show()
        }

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnSave.setOnClickListener {
            val updated = ReservationEntity(
                id = id,
                groomerId = groomerId,
                dogType = binding.spDogType.selectedItem as String,
                treatment = binding.spTreatment.selectedItem as String,
                dateTime = binding.tvDateTime.text.toString(),
                note = binding.etNote.text?.toString()
            )
            viewModel.updateReservation(updated)
            dismiss()
        }

        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ID = "id"
        private const val ARG_GROOMER_ID = "groomerId"
        private const val ARG_DOG_TYPE = "dogType"
        private const val ARG_TREATMENT = "treatment"
        private const val ARG_DATETIME = "dateTime"
        private const val ARG_NOTE = "note"

        fun newInstance(res: ReservationEntity) = EditReservationDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_ID, res.id)
                putLong(ARG_GROOMER_ID, res.groomerId)
                putString(ARG_DOG_TYPE, res.dogType)
                putString(ARG_TREATMENT, res.treatment)
                putString(ARG_DATETIME, res.dateTime)
                putString(ARG_NOTE, res.note)
            }
        }
    }
}
