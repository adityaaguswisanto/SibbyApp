package com.sibbya.sibbya.ui.home.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sibbya.sibbya.R
import com.sibbya.sibbya.data.responses.settings.Settings
import com.sibbya.sibbya.databinding.SettingsItemBinding

class SettingsAdapter(
    private val callBack: (name: String) -> Unit
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    private val list = listOf(
        Settings("1", "Membuat Akun Rumah Sakit", R.drawable.ic_people),
        Settings("2", "Privacy Policy", R.drawable.ic_privacy),
        Settings("3", "Logout", R.drawable.ic_logout),
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