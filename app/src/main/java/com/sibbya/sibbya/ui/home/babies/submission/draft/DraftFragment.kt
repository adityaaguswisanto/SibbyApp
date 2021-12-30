package com.sibbya.sibbya.ui.home.babies.submission.draft

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sibbya.sibbya.data.helper.*
import com.sibbya.sibbya.data.network.Resource
import com.sibbya.sibbya.databinding.FragmentDraftBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@AndroidEntryPoint
class DraftFragment : Fragment(), UploadRequestBody.UploadCallback {

    private val args by navArgs<DraftFragmentArgs>()
    private val viewModel by viewModels<DraftViewModel>()
    private lateinit var binding: FragmentDraftBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDraftBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.pdfResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast("Berhasil mengirimkan PDF draft bayi")
                        findNavController().navigate(DraftFragmentDirections.actionDraftFragmentToBabiesFragment())
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                    btnSend.invisible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                    btnSend.visible()
                }
                is Resource.Failure -> handleApiError(it) {
                    pdf()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
        txtSelectedPdf.setOnClickListener {
            pickPdf.launch(
                arrayOf(
                    "application/pdf",
                )
            )
        }

        txtSelectedPdf.addTextChangedListener {
            btnSend.enable(pdfUri != null)
        }

        btnSend.setOnClickListener {
            requireContext().alertDialog(
                "Tekan Ya jika anda ingin mengirim draft Kartu Keluarga Bayi",
                "Ya",
                "Tidak"
            ) { _, _ ->
                pdf()
            }
        }
    }

    private fun pdf() {

        val pdf = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(pdfUri!!)
        )
        FileInputStream(requireContext().fileDescriptor(pdfUri!!)).copyTo(
            FileOutputStream(pdf)
        )

        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.pdf(
                "Bearer $it",
                args.babies.id_baby,
                args.babies.user.id,
                args.babies.name.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                MultipartBody.Part.createFormData(
                    "pdf",
                    pdf.name,
                    UploadRequestBody(pdf, "application/pdf", this@DraftFragment)
                ),
                "2".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            )
        })
    }

    private val pickPdf = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) {
        it?.let {
            pdfUri = it
            binding.txtSelectedPdf.text = pdfUri.toString()
        }
    }

    companion object {
        private var pdfUri: Uri? = null
    }

    override fun onProgressUpdate(percentage: Int) = with(binding) {
        progressBar.progress = percentage
    }

}