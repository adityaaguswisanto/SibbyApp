package com.sibbya.sibbya.ui.home.settings.register

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.sibbya.sibbya.data.helper.*
import com.sibbya.sibbya.data.network.Resource
import com.sibbya.sibbya.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.registerResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
                        edtName.text.clear()
                        edtUname.text.clear()
                        edtEmail.text.clear()
                        edtPassword.text.clear()
                        edtHospital.text.clear()
                        edtAddress.text.clear()
                        edtHp.text.clear()
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                    ivSubmit.invisible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                    ivSubmit.visible()
                }
                is Resource.Failure -> handleApiError(it) {
                    register()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {

        cbPassword.setOnClickListener {
            if (cbPassword.isChecked) edtPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance() else edtPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
        }

        ivSubmit.setOnClickListener {
            requireContext().alertDialog(
                "Tekan Ya jika ingin membuat akun baru",
                "Ya",
                "Tidak"
            ) { _, _ ->
                register()
            }
        }
    }

    private fun register() = with(binding) {

        val name = edtName.text.toString().trim()
        val uname = edtUname.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val rs = edtHospital.text.toString().trim()
        val address = edtAddress.text.toString().trim()
        val hp = edtHp.text.toString().trim()

        if (name.isEmpty() || uname.isEmpty() || email.isEmpty() || password.isEmpty() || rs.isEmpty() || address.isEmpty() || hp.isEmpty()) {
            requireContext().toast("Isi data secara lengkap !")
        } else {
            viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                viewModel.register(
                    "Bearer $it",
                    name,
                    uname,
                    email,
                    password,
                    rs,
                    address,
                    hp,
                    "1"
                )
            })
        }
    }

}