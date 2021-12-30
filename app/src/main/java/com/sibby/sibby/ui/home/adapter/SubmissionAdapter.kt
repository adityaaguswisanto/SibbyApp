package com.sibby.sibby.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sibby.sibby.R
import com.sibby.sibby.data.responses.home.Submission
import com.sibby.sibby.databinding.SubmissionItemBinding

class SubmissionAdapter(
    private val callBack: (name: String) -> Unit
) : RecyclerView.Adapter<SubmissionAdapter.ViewHolder>() {

    private val list = listOf(
        Submission("1", "Sedang Proses", "Periksa berkas yang diproses", R.drawable.process),
        Submission("2", "Penerimaan Berkas", "Periksa berkas yang diterima", R.drawable.approve),
        Submission("3", "Penolakan", "Periksa berkas yang ditolak", R.drawable.riject),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            SubmissionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            cl.setOnClickListener { callBack.invoke(item.name) }
            txtTitle.text = item.title
            txtDesc.text = item.desc
            imageView.setImageDrawable(ContextCompat.getDrawable(root.context, item.imageId))
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val binding = SubmissionItemBinding.bind(view)
    }
}