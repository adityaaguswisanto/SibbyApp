package com.sibbya.sibbya.ui.home.home.account

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.sibbya.sibbya.data.helper.*
import com.sibbya.sibbya.data.network.Resource
import com.sibbya.sibbya.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@AndroidEntryPoint
class AccountFragment : Fragment(), UploadRequestBody.UploadCallback {

    private val args by navArgs<AccountFragmentArgs>()
    private val viewModel by viewModels<AccountViewModel>()
    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.userResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
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
                    user()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
        if (args.user.profile == null) {
            Log.i("TAG", "updateUI: nullable")
        } else {
            ivProfile.loadImage("${urlAssets()}${args.user.profile}")
        }
        edtName.setText(args.user.name)
        edtUname.setText(args.user.uname)
        edtEmail.setText(args.user.email)
        edtAddress.setText(args.user.address)
        edtHp.setText(args.user.hp)

        txtProfile.setOnClickListener {
            pickProfile.launch("image/*")
        }

        ivSubmit.setOnClickListener {
            requireContext().alertDialog(
                "Tekan Ya jika ingin mengupdate data",
                "Ya",
                "Tidak"
            ) { _, _ ->
                user()
            }
        }
    }

    private val pickProfile = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            profileUri = it
            binding.ivProfile.loadImage(profileUri.toString())
            profile(profileUri)
        }
    }

    private fun profile(profileUri: Uri?) {
        val fprofile = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(profileUri!!)
        )
        FileInputStream(requireContext().fileDescriptor(profileUri)).copyTo(
            FileOutputStream(
                fprofile
            )
        )

        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.profile(
                "Bearer $it",
                MultipartBody.Part.createFormData(
                    "profile",
                    fprofile.name,
                    UploadRequestBody(fprofile, "image", this@AccountFragment)
                )
            )
        })
    }

    private fun user() = with(binding) {
        val name = edtName.text.toString().trim()
        val uname = edtUname.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val address = edtAddress.text.toString().trim()
        val hp = edtHp.text.toString().trim()

        if (name.isEmpty() || uname.isEmpty() || email.isEmpty() || address.isEmpty() || hp.isEmpty()) {
            requireContext().toast("Tidak boleh kosong !")
        } else {
            viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                viewModel.user(
                    "Bearer $it",
                    name,
                    uname,
                    email,
                    address,
                    hp
                )
            })
        }
    }

    companion object {
        private var profileUri: Uri? = null
    }

    override fun onProgressUpdate(percentage: Int) = with(binding) {
        progressBar.progress = percentage
    }

}