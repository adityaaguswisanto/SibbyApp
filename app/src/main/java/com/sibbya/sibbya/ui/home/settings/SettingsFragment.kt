package com.sibbya.sibbya.ui.home.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sibbya.sibbya.data.helper.*
import com.sibbya.sibbya.data.network.Resource
import com.sibbya.sibbya.databinding.FragmentSettingsBinding
import com.sibbya.sibbya.ui.auth.AuthActivity
import com.sibbya.sibbya.ui.home.settings.adapter.SettingsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel by viewModels<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() {
        viewModel.logoutResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
                        logout()
                        requireActivity().launchActivity(AuthActivity::class.java)
                    }
                }
                is Resource.Loading -> {
                    Log.i("TAG", "setupObserver: Loading")
                }
                is Resource.Dismiss -> {
                    Log.i("TAG", "setupObserver: Dismiss")
                }
                is Resource.Failure -> handleApiError(it) {
                    clear()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
        settingsAdapter = SettingsAdapter {
            when (it) {
                "1" -> {
                    findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToRegisterFragment())
                }
                "2" -> {
                    val url = Intent(Intent.ACTION_VIEW)
                    url.data =
                        Uri.parse("https://www.privacypolicyonline.com/live.php?token=NM2Gs99OC2QhqyzlL13oUkILkKw4FWsT")
                    startActivity(url)
                }
                "3" -> {
                    requireContext().alertDialog(
                        "Tekan Ya jika anda ingin logout",
                        "Ya",
                        "Tidak"
                    ) { _, _ ->
                        clear()
                    }
                }
            }
        }
        rvSettings.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = settingsAdapter
        }
    }

    private fun clear() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.logout(
                "Bearer $it"
            )
        })
    }

}