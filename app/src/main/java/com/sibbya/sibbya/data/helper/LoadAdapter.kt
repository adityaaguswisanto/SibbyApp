package com.sibbya.sibbya.data.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sibbya.sibbya.databinding.LoadStateItemBinding

class LoadAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadAdapter.MyViewHolder>() {

    override fun onBindViewHolder(holder: MyViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = MyViewHolder(
        LoadStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), retry
    )

    inner class MyViewHolder(
        private val binding: LoadStateItemBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) = with(binding) {
            if (loadState is LoadState.Error) {
                txtError.text = loadState.error.localizedMessage
            }
            progressBar.visible(loadState is LoadState.Loading)
            btnRetry.visible(loadState is LoadState.Error)
            txtError.visible(loadState is LoadState.Error)
            btnRetry.setOnClickListener {
                retry()
            }
        }
    }
}