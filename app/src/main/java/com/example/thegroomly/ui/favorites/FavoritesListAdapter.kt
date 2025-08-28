package com.example.thegroomly.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.thegroomly.data.local.entities.GroomerEntity
import com.example.thegroomly.databinding.ItemGroomerBinding

class FavoritesListAdapter :
    ListAdapter<GroomerEntity, FavoritesListAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<GroomerEntity>() {
            override fun areItemsTheSame(o: GroomerEntity, n: GroomerEntity) = o.id == n.id
            override fun areContentsTheSame(o: GroomerEntity, n: GroomerEntity) = o == n
        }
    }

    class VH(val b: ItemGroomerBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemGroomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.b.tvName.text = item.name
        holder.b.tvDistance.text = "${item.distanceMeters} m"
        holder.b.ivPhoto.setImageResource(item.photoRes)
    }
}