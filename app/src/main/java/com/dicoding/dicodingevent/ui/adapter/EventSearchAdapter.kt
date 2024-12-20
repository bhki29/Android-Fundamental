package com.dicoding.dicodingevent.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.ItemEventUpcomingBinding

class EventSearchAdapter(private val onItemClick: (Int) -> Unit): ListAdapter<ListEventsItem, EventSearchAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemEventUpcomingBinding, private val onItemClick: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(event: ListEventsItem){
            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.imgEvent)
            binding.tvEvent.text = event.name
            binding.tvCategory.text = event.category
            binding.tvOwner.text = "Penyelenggara : ${event.ownerName}"
            itemView.setOnClickListener {
                onItemClick(event.id)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemEventUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        val ivFav = holder.itemView.findViewById<ImageView>(R.id.iv_fav)

        ivFav.visibility = View.GONE
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}