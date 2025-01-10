package com.example.myevent

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EventAdapter : ListAdapter<ListEventsItem, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvEventName)
        val tvDate: TextView = itemView.findViewById(R.id.tvEventBeginTime)
        val ivMediaCover: ImageView = itemView.findViewById(R.id.ivMediaCover)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = getItem(position)
                    val intent = Intent(itemView.context, ActivityDetail::class.java) // Update to your DetailEventActivity
                    intent.putExtra("id", event.id) // Pass the event ID
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.tvName.text = event.name
        holder.tvDate.text = event.beginTime

        Glide.with(holder.itemView.context)
            .load(event.mediaCover)
            .into(holder.ivMediaCover)
    }

    class EventDiffCallback : DiffUtil.ItemCallback<ListEventsItem>() {
        override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
            return oldItem == newItem
        }
    }
}
