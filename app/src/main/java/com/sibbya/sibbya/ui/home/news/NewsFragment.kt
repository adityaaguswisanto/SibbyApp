package com.sibbya.sibbya.ui.home.news

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.sibbya.sibbya.R
import com.sibbya.sibbya.data.helper.LoadAdapter
import com.sibbya.sibbya.data.helper.toast
import com.sibbya.sibbya.data.helper.visible
import com.sibbya.sibbya.data.responses.data.News
import com.sibbya.sibbya.databinding.FragmentNewsBinding
import com.sibbya.sibbya.ui.home.news.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment(), NewsAdapter.OnItemClickListener {

    private val viewModel by viewModels<NewsViewModel>()
    private lateinit var binding: FragmentNewsBinding

    private val newsAdapter = NewsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        news()

        rvNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = newsAdapter.withLoadStateFooter(
                footer = LoadAdapter { newsAdapter.retry() },
            )
        }

        newsAdapter.addLoadStateListener {
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
            newsAdapter.retry()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                        lifecycleScope.launch {
                            viewModel.index(
                                "Bearer $it",
                                newText
                            ).collectLatest {
                                newsAdapter.submitData(it)
                            }
                        }
                    })
                } else {
                    news()
                }
                return true
            }

        })
    }

    private fun news() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.index(
                    "Bearer $it",
                    ""
                ).collectLatest {
                    newsAdapter.submitData(it)
                }
            }
        })
    }

    override fun onItemClick(news: News) {
        findNavController().navigate(
            NewsFragmentDirections.actionNewsFragmentToDetailsFragment(
                news
            )
        )
    }

}