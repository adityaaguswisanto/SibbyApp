package com.sibby.sibby.ui.home.babies.failure

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sibby.sibby.data.helper.*
import com.sibby.sibby.data.network.Resource
import com.sibby.sibby.databinding.FragmentFailureBinding
import com.sibby.sibby.ui.home.submission.SubmissionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@AndroidEntryPoint
class FailureFragment : Fragment(), UploadRequestBody.UploadCallback {

    private val args by navArgs<FailureFragmentArgs>()
    private val viewModel by viewModels<SubmissionViewModel>()
    private lateinit var binding: FragmentFailureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFailureBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.submissionResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast("Newborn submission successful")
                        findNavController().navigate(FailureFragmentDirections.actionFailureFragmentToBabiesFragment())
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
                    update()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {

        edtName.setText(args.babies.name)
        when (args.babies.gender) {
            "1" -> {
                rbMale.isChecked = true
            }
            "2" -> {
                rbFemale.isChecked = true
            }
        }
        edtDad.setText(args.babies.dad)
        edtMom.setText(args.babies.mom)

        rbMale.setOnClickListener {
            rbMale.isChecked = true
            gender = "1"
        }

        rbFemale.setOnClickListener {
            rbFemale.isChecked = true
            gender = "2"
        }

        ivSkl.setOnClickListener {
            pickSkl.launch("image/*")
        }

        ivKtpd.setOnClickListener {
            pickKtpd.launch("image/*")
        }

        ivKtpm.setOnClickListener {
            pickKtpm.launch("image/*")
        }

        ivKk.setOnClickListener {
            pickKk.launch("image/*")
        }

        ivBk.setOnClickListener {
            pickBk.launch("image/*")
        }

        btnSend.setOnClickListener {
            requireContext().alertDialog(
                "Tekan Ya jika anda ingin mengupdate perbaikan berkas",
                "Ya",
                "Tidak"
            ) { _, _ ->
                update()
            }
        }
    }

    private val pickSkl = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            sklUri = it
            binding.ivSkl.loadImage(sklUri.toString())
        }
    }

    private val pickKtpd = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            ktpdUri = it
            binding.ivKtpd.loadImage(ktpdUri.toString())
        }
    }

    private val pickKtpm = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            ktpmUri = it
            binding.ivKtpm.loadImage(ktpmUri.toString())
        }
    }

    private val pickKk = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            kkUri = it
            binding.ivKk.loadImage(kkUri.toString())
        }
    }

    private val pickBk = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            bkUri = it
            binding.ivBk.loadImage(bkUri.toString())
        }
    }

    private fun update() = with(binding) {
        val name = edtName.text.toString().trim()
        val dad = edtDad.text.toString().trim()
        val mom = edtMom.text.toString().trim()

        if (name.isEmpty() || dad.isEmpty() || mom.isEmpty() || sklUri == null || ktpdUri == null || ktpmUri == null || kkUri == null || bkUri == null) {
            requireContext().toast("Mohon isi data dengan lengkap !")
        } else {
            val fskl = File(
                requireContext().cacheDir,
                requireContext().contentResolver.getFileName(sklUri!!)
            )
            FileInputStream(requireContext().fileDescriptor(sklUri!!)).copyTo(
                FileOutputStream(fskl)
            )

            val fktpd = File(
                requireContext().cacheDir,
                requireContext().contentResolver.getFileName(ktpdUri!!)
            )
            FileInputStream(requireContext().fileDescriptor(ktpdUri!!)).copyTo(
                FileOutputStream(
                    fktpd
                )
            )

            val fktpm = File(
                requireContext().cacheDir,
                requireContext().contentResolver.getFileName(ktpmUri!!)
            )
            FileInputStream(requireContext().fileDescriptor(ktpmUri!!)).copyTo(
                FileOutputStream(
                    fktpm
                )
            )

            val fkk = File(
                requireContext().cacheDir,
                requireContext().contentResolver.getFileName(kkUri!!)
            )
            FileInputStream(requireContext().fileDescriptor(kkUri!!)).copyTo(
                FileOutputStream(fkk)
            )

            val fbk = File(
                requireContext().cacheDir,
                requireContext().contentResolver.getFileName(bkUri!!)
            )
            FileInputStream(requireContext().fileDescriptor(bkUri!!)).copyTo(
                FileOutputStream(fbk)
            )

            viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                viewModel.update(
                    "Bearer $it",
                    args.babies.id_baby,
                    name.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    gender.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    dad.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    mom.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    MultipartBody.Part.createFormData(
                        "skl",
                        fskl.name,
                        UploadRequestBody(fskl, "image", this@FailureFragment)
                    ),
                    MultipartBody.Part.createFormData(
                        "ktpd",
                        fktpd.name,
                        UploadRequestBody(fktpd, "image", this@FailureFragment)
                    ),
                    MultipartBody.Part.createFormData(
                        "ktpm",
                        fktpm.name,
                        UploadRequestBody(fktpm, "image", this@FailureFragment)
                    ),
                    MultipartBody.Part.createFormData(
                        "kk",
                        fkk.name,
                        UploadRequestBody(fkk, "image", this@FailureFragment)
                    ),
                    MultipartBody.Part.createFormData(
                        "bk",
                        fbk.name,
                        UploadRequestBody(fbk, "image", this@FailureFragment)
                    ),
                )
            })
        }
    }

    companion object {
        private lateinit var gender: String
        private var sklUri: Uri? = null
        private var ktpdUri: Uri? = null
        private var ktpmUri: Uri? = null
        private var kkUri: Uri? = null
        private var bkUri: Uri? = null
    }

    override fun onProgressUpdate(percentage: Int) = with(binding) {
        progressBar.progress = percentage
    }

}