package com.sibbya.sibbya.ui.home.babies.submission.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.sibbya.sibbya.data.helper.loadImage
import com.sibbya.sibbya.data.helper.urlAssets
import com.sibbya.sibbya.databinding.FragmentViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewFragment : Fragment() {

    private val args by navArgs<ViewFragmentArgs>()
    private lateinit var binding: FragmentViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {
        ivFiles.loadImage("${urlAssets()}${args.image}")
    }

}