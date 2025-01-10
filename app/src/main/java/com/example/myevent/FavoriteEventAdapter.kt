package com.example.myevent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myevent.databinding.ItemFavoriteEventBinding

class FavoriteEventAdapter(
    private val onRemoveClick: (FavoriteEventEntity) -> Unit,
    private val onItemClick: (FavoriteEventEntity) -> Unit
) : ListAdapter<FavoriteEventEntity, FavoriteEventAdapter.FavoriteEventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ItemFavoriteEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteEventViewHolder(private val binding: ItemFavoriteEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteEventEntity) {
            binding.tvEventName.text = event.name
            binding.tvEventBeginTime.text = event.beginTime
            event.mediaCover?.let { coverUrl ->
                Glide.with(binding.ivMediaCover.context)
                    .load(coverUrl)
                    .into(binding.ivMediaCover)
            }

            binding.btnDeleteFavorite.setOnClickListener {
                onRemoveClick(event)
            }
            binding.root.setOnClickListener {
                onItemClick(event)
            }
        }
    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEventEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteEventEntity, newItem: FavoriteEventEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteEventEntity, newItem: FavoriteEventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
