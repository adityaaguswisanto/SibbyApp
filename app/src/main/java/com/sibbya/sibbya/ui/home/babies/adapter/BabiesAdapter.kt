package com.sibbya.sibbya.ui.home.babies.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sibbya.sibbya.data.helper.formatDate
import com.sibbya.sibbya.data.responses.data.Babies
import com.sibbya.sibbya.databinding.BabiesItemBinding

class BabiesAdapter(
    private val listener: OnItemClickListener
) : PagingDataAdapter<Babies, BabiesAdapter.MyViewHolder>(BabiesComparator) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            BabiesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MyViewHolder(private val binding: BabiesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(babies: Babies) = with(binding) {
            when (babies.status) {
                "1" -> {
                    txtName.setTextColor(Color.parseColor("#01AB6B"))
                    view.setBackgroundColor(Color.parseColor("#01AB6B"))
                    ivRight.setColorFilter(Color.parseColor("#01AB6B"))
                }
                "2" -> {
                    txtName.setTextColor(Color.parseColor("#FFC727"))
                    view.setBackgroundColor(Color.parseColor("#FFC727"))
                    ivRight.setColorFilter(Color.parseColor("#FFC727"))
                }
                "3" -> {
                    txtName.setTextColor(Color.parseColor("#FF725E"))
                    view.setBackgroundColor(Color.parseColor("#FF725E"))
                    ivRight.setColorFilter(Color.parseColor("#FF725E"))
                }
            }
            txtName.text = babies.name
            txtParent.text = "Anak dari Ayah ${babies.dad} dan Ibu ${babies.mom}"
            txtDateAndRs.text = "${babies.user.rs} ~ ${formatDate(babies.created_at)}"
        }
    }

    object BabiesComparator : DiffUtil.ItemCallback<Babies>() {
        override fun areItemsTheSame(oldItem: Babies, newItem: Babies): Boolean {
            return oldItem.id_baby == newItem.id_baby
        }

        override fun areContentsTheSame(oldItem: Babies, newItem: Babies): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(babies: Babies)
    }

}