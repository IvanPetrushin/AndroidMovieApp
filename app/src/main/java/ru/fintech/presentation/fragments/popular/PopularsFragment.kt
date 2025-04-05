package ru.fintech.presentation.fragments.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.fintech.R
import ru.fintech.databinding.FragmentScreenBinding
import ru.fintech.entities.dto.FilmDTO
import ru.fintech.presentation.adapters.PopularsListAdapter
import ru.fintech.presentation.adapters.RecyclerClickListener
import ru.fintech.presentation.adapters.SupportedView

typealias Loaded = PopularsViewModel.State.Loaded
typealias Loading = PopularsViewModel.State.Loading
typealias Error = PopularsViewModel.State.Error

@AndroidEntryPoint
class PopularsFragment : Fragment() {
    private val viewModel: PopularsViewModel by viewModels()
    private lateinit var binding: FragmentScreenBinding

    private val dataset = mutableListOf<FilmDTO>()
    private val adapter = PopularsListAdapter()

    private var page = 1
    private var pageCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarTextView.text = resources.getString(R.string.populars)
        bindToViewModel()
        setupRecycleView()
        setupFind()
    }

    private fun bindToViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.recyclerView.visibility =
                if (state is Loaded) View.VISIBLE else View.INVISIBLE
            if (state is Loaded) {
                val beforeSize = dataset.size
                state.filmsData.films.forEach { filmDTO ->
                    dataset.add(filmDTO)
                }
                adapter.submitList(dataset.toList())
                pageCount = state.filmsData.pagesCount
                adapter.notifyItemRangeInserted(beforeSize, dataset.size - beforeSize)
            }

            adapter.setItemListener(object : RecyclerClickListener {
                override fun onItemClick(filmId: Int) {
                    viewModel.onItemClicked(filmId)
                }

                override fun onItemRemoveClick(filmId: Int) {
                    viewModel.onItemRemoveClicked(filmId)
                }
            })

            with(binding.stateViewLayout) {
                stateView.visibility =
                    if (state is Loaded) View.GONE else View.VISIBLE
                val imageRes =
                    if (state is Loading) R.drawable.loading_animation else R.drawable.network
                statusImageView.setImageResource(imageRes)

                errorTextView.visibility =
                    if (state is Error) View.VISIBLE else View.GONE

                retryButton.visibility =
                    if (state is Error) View.VISIBLE else View.GONE

                retryButton.setOnClickListener { viewModel.onRetryButtonPressed(page) }
            }

            binding.bottomNavigation.favouritesButton.setOnClickListener {
                it.findNavController().navigate(R.id.action_popularsFragment_to_favouritesFragment)
            }
            binding.bottomNavigation.profileButton.setOnClickListener {
                it.findNavController().navigate(R.id.action_popularsFragment_to_profileFragment)
            }

        }
    }

    private fun setupFind() {
        with(binding) {
            SupportedView.setupSearchViewCondition(
                searchView = searchView,
                searchLoop = searchLoop,
                cancelLoop = cancelLoop,
                toolbarTextView = toolbarTextView
            )

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newChar: String): Boolean {
                    val filmsToFindList: MutableList<FilmDTO> = mutableListOf()

                    dataset.forEach {
                        if (it.nameRu.startsWith(newChar, true) && newChar.isNotEmpty()) {
                            filmsToFindList.add(it)
                            if (SupportedView.isKeyboardOpen(requireActivity()))
                                recyclerView.smoothScrollToPosition(0)
                        }
                    }

                    val remainingList = dataset - filmsToFindList
                    val sortedList = filmsToFindList + remainingList
                    adapter.submitList(sortedList)
                    return true
                }
            })
        }
    }

    private fun setupRecycleView() {
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition >= adapter.itemCount - 10 && page < pageCount) {
                    page++
                    viewModel.onScrolledEnough(page = page)
                }
            }
        })
    }

}