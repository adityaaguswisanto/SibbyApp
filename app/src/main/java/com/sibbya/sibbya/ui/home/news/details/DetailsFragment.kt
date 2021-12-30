package com.sibbya.sibbya.ui.home.news.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.sibbya.sibbya.data.helper.formatDate
import com.sibbya.sibbya.data.helper.loadImage
import com.sibbya.sibbya.data.helper.urlNews
import com.sibbya.sibbya.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {
        toolbar.title = args.news.title
        ivImage.loadImage("${urlNews()}${args.news.image}")
        txtDateAuthor.text =
            "${formatDate(args.news.created_at)} - Author : Disdukcapil Kota Tangerang"
        txtBody.text = args.news.body
    }

}