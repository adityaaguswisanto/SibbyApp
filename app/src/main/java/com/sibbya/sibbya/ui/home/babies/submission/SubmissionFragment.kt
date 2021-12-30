package com.sibbya.sibbya.ui.home.babies.submission

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
import androidx.navigation.fragment.navArgs
import com.sibbya.sibbya.data.helper.*
import com.sibbya.sibbya.data.network.Resource
import com.sibbya.sibbya.databinding.FragmentSubmissionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubmissionFragment : Fragment() {

    private val args by navArgs<SubmissionFragmentArgs>()
    private val viewModel by viewModels<SubmissionViewModel>()
    private lateinit var binding: FragmentSubmissionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubmissionBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() {
        viewModel.submissionResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast("Berhasil melakukan penolakan berkas")
                        findNavController().navigate(SubmissionFragmentDirections.actionSubmissionFragmentToBabiesFragment())
                    }
                }
                is Resource.Loading -> {
                    Log.i("TAG", "setupObserver: Loading")
                }
                is Resource.Dismiss -> {
                    Log.i("TAG", "setupObserver: Dismiss")
                }
                is Resource.Failure -> handleApiError(it) {
                    riject()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
        edtName.setText(args.babies.name)

        if (args.babies.gender == "1") {
            rbMale.isChecked = true
        } else {
            rbFemale.isChecked = true
        }

        edtDad.setText(args.babies.dad)
        edtMom.setText(args.babies.mom)

        ivSkl.loadImage("${urlAssets()}${args.babies.skl}")
        ivKtpd.loadImage("${urlAssets()}${args.babies.ktpd}")
        ivKtpm.loadImage("${urlAssets()}${args.babies.ktpm}")
        ivKk.loadImage("${urlAssets()}${args.babies.kk}")
        ivBk.loadImage("${urlAssets()}${args.babies.bk}")

        ivSkl.setOnClickListener {
            findNavController().navigate(
                SubmissionFragmentDirections.actionSubmissionFragmentToViewFragment(
                    args.babies.skl
                )
            )
        }
        ivKtpd.setOnClickListener {
            findNavController().navigate(
                SubmissionFragmentDirections.actionSubmissionFragmentToViewFragment(
                    args.babies.ktpd
                )
            )
        }
        ivKtpm.setOnClickListener {
            findNavController().navigate(
                SubmissionFragmentDirections.actionSubmissionFragmentToViewFragment(
                    args.babies.ktpm
                )
            )
        }
        ivKk.setOnClickListener {
            findNavController().navigate(
                SubmissionFragmentDirections.actionSubmissionFragmentToViewFragment(
                    args.babies.kk
                )
            )
        }
        ivBk.setOnClickListener {
            findNavController().navigate(
                SubmissionFragmentDirections.actionSubmissionFragmentToViewFragment(
                    args.babies.bk
                )
            )
        }

        btnRiject.setOnClickListener {
            requireContext().alertDialog(
                "Tekan Ya jika anda ingin menolak berkas",
                "Ya",
                "Tidak"
            ) { _, _ ->
                riject()
            }
        }

        btnDraft.setOnClickListener {
            findNavController().navigate(
                SubmissionFragmentDirections.actionSubmissionFragmentToDraftFragment(
                    args.babies
                )
            )
        }
    }

    private fun riject() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.riject(
                "Bearer $it",
                args.babies.id_baby,
                args.babies.user.id,
                args.babies.name,
                "3"
            )
        })
    }

}