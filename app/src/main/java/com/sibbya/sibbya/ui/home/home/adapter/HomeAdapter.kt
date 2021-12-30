package com.sibbya.sibbya.ui.home.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sibbya.sibbya.data.helper.formatDate
import com.sibbya.sibbya.data.helper.loadImage
import com.sibbya.sibbya.data.helper.urlAssets
import com.sibbya.sibbya.data.helper.urlNews
import com.sibbya.sibbya.data.responses.data.News
import com.sibbya.sibbya.databinding.NewsItemBinding

class HomeAdapter(
    private val news: List<News>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            NewsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(news[position])
    }

    override fun getItemCount(): Int {
        return news.size
    }

    inner class MyViewHolder(private val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: News) = with(binding) {
            ivImage.loadImage("${urlNews()}${news.image}")
            txtTitle.text = news.title
            txtBody.text = news.body
            txtDate.text = formatDate(news.created_at)

            binding.root.setOnClickListener {
                listener.onItemClick(news)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(news: News)
    }


}