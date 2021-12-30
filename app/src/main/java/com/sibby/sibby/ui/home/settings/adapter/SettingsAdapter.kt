package com.sibby.sibby.ui.home.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sibby.sibby.R
import com.sibby.sibby.data.responses.settings.Settings
import com.sibby.sibby.databinding.SettingsItemBinding

class SettingsAdapter(
    private val callBack: (name: String) -> Unit
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    private val list = listOf(
        Settings("1", "Privacy Policy", R.drawable.ic_privacy),
        Settings("2", "Logout", R.drawable.ic_logout),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            SettingsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            clSettings.setOnClickListener { callBack.invoke(item.name) }
            txtTitle.text = item.title
            ivIcon.setImageDrawable(ContextCompat.getDrawable(root.context, item.imageId))
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val binding = SettingsItemBinding.bind(view)
    }
}