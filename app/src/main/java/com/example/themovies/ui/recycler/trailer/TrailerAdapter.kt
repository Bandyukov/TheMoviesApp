package com.example.themovies.ui.recycler.trailer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themovies.R
import com.example.themovies.core.models.trailer.Trailer
import com.squareup.picasso.Picasso

class TrailerAdapter : RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {

    private var trailers = listOf<Trailer>()

    companion object {
        private lateinit var onTrailerClickListener: OnTrailerClickListener

        fun setOnTrailerClickListener(onTrailerClickListener: OnTrailerClickListener) {
            this.onTrailerClickListener = onTrailerClickListener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        return TrailerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val trailer = trailers[position]
        holder.bind(trailer)
    }

    override fun getItemCount(): Int {
        return trailers.size
    }

    fun setTrailers(trailers: List<Trailer>) {
        this.trailers = trailers
        notifyDataSetChanged()
    }

    class TrailerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val address = "https://www.freeiconspng.com/uploads/play-button-icon-png-8.png"
        private val textViewTrailerName: TextView = itemView.findViewById(R.id.textViewTrailerName)
        private val image: ImageView = itemView.findViewById(R.id.imageViewPlayButton)

        init {
            itemView.setOnClickListener {
                onTrailerClickListener.onShortClick(adapterPosition)
            }

            itemView.setOnLongClickListener {
                onTrailerClickListener.onLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }

        fun bind(trailer: Trailer) {
            textViewTrailerName.text = trailer.name
            Picasso.get().load(address).into(image)
        }

        companion object {
            fun from(parent: ViewGroup) : TrailerViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.trailer_item, parent, false)
                return TrailerViewHolder(view)
            }
        }
    }
}