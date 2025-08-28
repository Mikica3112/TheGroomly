package com.example.thegroomly.ui.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.thegroomly.data.local.entities.ReservationEntity
import com.example.thegroomly.databinding.ItemReservationBinding

class ReservationsAdapter(
    private var items: List<ReservationEntity>,
    private val resolveGroomerName: (Long) -> String,
) : RecyclerView.Adapter<ReservationsAdapter.VH>() {

    class VH(val b: ItemReservationBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val salon = resolveGroomerName(item.groomerId)
        holder.b.tvLine1.text = "$salon • ${item.dateTime}"
        holder.b.tvLine2.text = "${item.dogType} • ${item.treatment}"
        holder.b.tvLine3.text = item.note?.takeIf { it.isNotBlank() } ?: "—"
    }

    override fun getItemCount() = items.size

    fun submit(newItems: List<ReservationEntity>) {
        items = newItems
        notifyDataSetChanged()
    }
}
