package com.sibby.sibby.ui.home.home.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.sibby.sibby.data.helper.LoadAdapter
import com.sibby.sibby.data.helper.toast
import com.sibby.sibby.data.helper.visible
import com.sibby.sibby.data.responses.data.Babies
import com.sibby.sibby.databinding.FragmentStatusBinding
import com.sibby.sibby.ui.home.babies.adapter.BabiesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatusFragment : Fragment(), BabiesAdapter.OnItemClickListener {

    private val args by navArgs<StatusFragmentArgs>()
    private val viewModel by viewModels<StatusViewModel>()
    private lateinit var binding: FragmentStatusBinding

    private val babiesAdapter = BabiesAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatusBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {
        babies()

        rvStatus.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = babiesAdapter.withLoadStateFooter(
                footer = LoadAdapter { babiesAdapter.retry() },
            )
        }

        babiesAdapter.addLoadStateListener {
            progressBar.visible(it.source.refresh is LoadState.Loading)
            ivRetry.visible(it.source.refresh is LoadState.Error)

            val errorState = it.source.append as? LoadState.Error
                ?: it.source.prepend as? LoadState.Error
                ?: it.append as? LoadState.Error
                ?: it.prepend as? LoadState.Error
            errorState?.let { state ->
                requireContext().toast("\uD83D\uDE28 Wooops ${state.error}")
            }
        }

        ivRetry.setOnClickListener {
            babiesAdapter.retry()
        }
    }

    private fun babies() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.status(
                    "Bearer $it",
                    args.status
                ).collectLatest {
                    babiesAdapter.submitData(it)
                }
            }
        })
    }

    override fun onItemClick(babies: Babies) {
        if (babies.status == "3") {
            findNavController().navigate(
                StatusFragmentDirections.actionStatusFragmentToFailureFragment(
                    babies
                )
            )
        }
    }

}