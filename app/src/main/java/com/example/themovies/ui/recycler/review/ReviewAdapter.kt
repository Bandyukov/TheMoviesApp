package com.example.themovies.ui.recycler.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themovies.R
import com.example.themovies.core.models.review.Review

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var reviews = listOf<Review>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    fun setReviews(reviews: List<Review>) {
        this.reviews = reviews
        notifyDataSetChanged()
    }

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewAuthor: TextView = itemView.findViewById(R.id.textViewAuthor)
        private val textViewContent: TextView = itemView.findViewById(R.id.textViewContent)

        fun bind(review: Review) {
            textViewAuthor.text = review.author
            textViewContent.text = review.content
        }

        companion object {
            fun from(parent: ViewGroup): ReviewViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
                return ReviewViewHolder(view)
            }
        }
    }
}