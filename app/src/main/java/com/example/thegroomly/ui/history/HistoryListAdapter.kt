package com.example.thegroomly.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thegroomly.R
import com.example.thegroomly.data.local.entities.ReservationEntity

class HistoryListAdapter(
    private var items: List<ReservationEntity>
) : RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvSub: TextView = view.findViewById(R.id.tvSub)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = "Salon #${item.groomerId}"
        holder.tvSub.text = "${item.dogType} • ${item.treatment} • ${item.dateTime}"
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<ReservationEntity>) {
        items = newItems
        notifyDataSetChanged()
    }
}
