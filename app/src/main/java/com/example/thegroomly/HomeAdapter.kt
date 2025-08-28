package com.example.thegroomly

import android.widget.Filter
import android.widget.Filterable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.thegroomly.databinding.ItemGroomerBinding

class HomeAdapter(
    private val originalList: List<Groomer>,
    private val onClick: (Groomer) -> Unit
) : RecyclerView.Adapter<HomeAdapter.VH>(), Filterable {

    // lista koju RecyclerView stvarno prikazuje
    private var filteredList: List<Groomer> = originalList

    inner class VH(private val binding: ItemGroomerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Groomer) {
            binding.tvName.text = item.name
            binding.tvDistance.text = item.distance
            binding.ivPhoto.setImageResource(item.photoRes)
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemGroomerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val query = constraint?.toString()?.lowercase()?.trim() ?: ""
            val resultList = if (query.isEmpty()) {
                originalList
            } else {
                originalList.filter {
                    it.name.lowercase().contains(query)
                }
            }
            return FilterResults().apply { values = resultList }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            @Suppress("UNCHECKED_CAST")
            filteredList = results?.values as? List<Groomer> ?: emptyList()
            notifyDataSetChanged()
        }
    }
}
