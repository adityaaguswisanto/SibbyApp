package com.sibbya.sibbya.ui.auth.forgot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sibbya.sibbya.data.helper.*
import com.sibbya.sibbya.data.network.Resource
import com.sibbya.sibbya.databinding.FragmentForgotBinding
import com.sibbya.sibbya.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotFragment : Fragment() {

    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var binding: FragmentForgotBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.forgotResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
                        edtEmail.text?.clear()
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                    btnForgot.invisible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                    btnForgot.visible()
                }
                is Resource.Failure -> handleApiError(it) {
                    forgot()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
        edtEmail.addTextChangedListener {
            btnForgot.enable(it.toString().isNotEmpty())
        }
        btnForgot.setOnClickListener {
            forgot()
        }
    }

    private fun forgot() = with(binding) {
        val email = edtEmail.text.toString().trim()
        viewModel.forgot(email)
    }

}