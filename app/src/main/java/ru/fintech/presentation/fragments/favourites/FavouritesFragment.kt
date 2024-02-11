package ru.fintech.presentation.fragments.favourites

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.fintech.R
import ru.fintech.databinding.FragmentScreenBinding
import ru.fintech.entities.dto.DescriptionDTO
import ru.fintech.presentation.adapters.FavouritesListAdapter
import ru.fintech.presentation.adapters.RecyclerClickListener
import ru.fintech.presentation.adapters.SupportedView

typealias Loaded = FavouritesViewModel.State.Loaded

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
    private val viewModel: FavouritesViewModel by viewModels()
    private lateinit var binding: FragmentScreenBinding

    private val adapter = FavouritesListAdapter()
    private var dataset: List<DescriptionDTO> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarTextView.text = resources.getString(R.string.favourites)
        bindToViewModel()
        setupFind()
    }

    private fun bindToViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.recyclerView.visibility =
                if (state is Loaded) View.VISIBLE else View.INVISIBLE

            dataset = if (state is Loaded)
                state.films
            else
                emptyList()
            adapter.submitList(dataset)
            adapter.setItemListener(object : RecyclerClickListener {
                override fun onItemClick(filmId: Int) {
                    viewModel.onItemClicked(filmId)
                }

                override fun onItemRemoveClick(filmId: Int) {
                    viewModel.onItemRemoveClicked(filmId)
                }
            })

            binding.bottomNavigation.popularsButton.setOnClickListener {
                it.findNavController().navigate(R.id.action_favouritesFragment_to_popularsFragment)
            }

            binding.recyclerView.adapter = adapter
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
                    val filmsToFindList: MutableList<DescriptionDTO> = mutableListOf()

                    dataset.forEach {
                        if (it.nameRu.startsWith(newChar, true)) {
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
}