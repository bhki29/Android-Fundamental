package com.dicoding.dicodingevent.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.databinding.ItemEventFinishedBinding

class EventFinishedAdapter(private val onItemClick: (Int) -> Unit, private val onFavoriteClick: (EventEntity) -> Unit): ListAdapter<EventEntity, EventFinishedAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemEventFinishedBinding, private val onItemClick: (Int) -> Unit): RecyclerView.ViewHolder(binding.root)  {
        @SuppressLint("SetTextI18n")
        fun bind(event: EventEntity){
            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.imgEvent)
            binding.tvEvent.text = event.title
            binding.tvCategory.text = event.category
            binding.tvOwner.text = "Penyelenggara : ${event.owner}"
            itemView.setOnClickListener {
                onItemClick(event.id)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemEventFinishedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        val ivFav = holder.itemView.findViewById<ImageView>(R.id.iv_fav)

        if (event.isFavorite) {
            ivFav.setImageDrawable(ContextCompat.getDrawable(ivFav.context, R.drawable.baseline_favorite_24))
        } else {
            ivFav.setImageDrawable(ContextCompat.getDrawable(ivFav.context, R.drawable.baseline_favorite_border_24))
        }

        ivFav.setOnClickListener {
            onFavoriteClick(event)
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}