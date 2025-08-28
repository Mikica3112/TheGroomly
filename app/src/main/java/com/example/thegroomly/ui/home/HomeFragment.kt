package com.example.thegroomly.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thegroomly.Groomer
import com.example.thegroomly.HomeAdapter
import com.example.thegroomly.R
import com.example.thegroomly.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sampleGroomers = listOf(
            Groomer("Nera", "1.2 km", R.drawable.ic_frizer),
            Groomer("Doggy", "850 m", R.drawable.ic_frizer),
            Groomer("Čupko", "2.5 km", R.drawable.ic_frizer)
        )

        adapter = HomeAdapter(sampleGroomers) { clickedGroomer ->
            Toast.makeText(
                requireContext(),
                "Odabrali ste: ${clickedGroomer.name}",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.rvGroomers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGroomers.adapter = adapter

        // poveži EditText s filtarom
        binding.etFilter.doAfterTextChanged { text ->
            adapter.filter.filter(text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
