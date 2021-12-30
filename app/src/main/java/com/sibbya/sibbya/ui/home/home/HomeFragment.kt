package com.sibbya.sibbya.ui.home.home

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sibbya.sibbya.data.helper.*
import com.sibbya.sibbya.data.network.Resource
import com.sibbya.sibbya.data.responses.data.News
import com.sibbya.sibbya.data.responses.data.User
import com.sibbya.sibbya.databinding.FragmentHomeBinding
import com.sibbya.sibbya.ui.home.adapter.SubmissionAdapter
import com.sibbya.sibbya.ui.home.home.adapter.HomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeAdapter.OnItemClickListener {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var submissionAdapter: SubmissionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.homeResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        user = it.value.user

                        if (it.value.user.profile == null) {
                            Log.i("TAG", "setupObserver: nullable")
                        } else {
                            ivProfile.loadImage("${urlAssets()}${it.value.user.profile}")
                        }

                        txtName.text = "Hi, ${it.value.user.name}"
                        rvNews.apply {
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            setHasFixedSize(true)
                            adapter = HomeAdapter(it.value.news, this@HomeFragment)
                        }
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                }
                is Resource.Failure -> handleApiError(it) {
                    home()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {

        permissionExternalStorage().launch(Manifest.permission.READ_EXTERNAL_STORAGE)

        home()

        submissionAdapter = SubmissionAdapter {
            when (it) {
                "1" -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToStatusFragment(
                            "1"
                        )
                    )
                }
                "2" -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToStatusFragment(
                            "2"
                        )
                    )
                }
                "3" -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToStatusFragment(
                            "3"
                        )
                    )
                }
            }
        }

        ivMore.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAccountFragment(
                    user
                )
            )
        }

        rvSubmission.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = submissionAdapter
        }

        btnHistories.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBabiesFragment())
        }
    }

    private fun home() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.home(
                "Bearer $it"
            )
        })
    }

    companion object {
        private lateinit var user: User
    }

    override fun onItemClick(news: News) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNewsFragment())
    }

}