package ru.fintech.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fintech.R
import ru.fintech.databinding.FilmItemBinding
import ru.fintech.entities.dto.DescriptionDTO
import ru.fintech.presentation.fragments.filmcard.FilmCardFragment
import ru.fintech.utils.SaveImageUtil
import java.io.File

class FavouritesListAdapter : ListAdapter<DescriptionDTO, FavouritesListAdapter.ItemViewHolder>(DiffCallback()) {

    private lateinit var listener: RecyclerClickListener
    fun setItemListener(listener: RecyclerClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            FilmItemBinding.inflate(LayoutInflater.from(parent.context)), parent.context
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener)
    }

    class ItemViewHolder(private val binding: FilmItemBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        private val saveImageUtil: SaveImageUtil = SaveImageUtil()
        fun bind(item: DescriptionDTO, listener: RecyclerClickListener) {
            with(binding) {
                itemName.text = item.nameRu
                itemImage.setImageURI(
                    File(
                        context.filesDir,
                        context.resources.getString(R.string.fileName, item.kinopoiskId)
                    ).toUri()
                )
                itemGenreYear.text = context.getString(
                    R.string.genreYear,
                    item.genres[0].genre.replaceFirstChar { it.uppercase() },
                    item.year
                )
                filmCard.setOnClickListener { view ->
                    view.findNavController().navigate(
                        R.id.action_favouritesFragment_to_filmCardFragment,
                        FilmCardFragment.createArguments(filmId = item.kinopoiskId)
                    )
                }
                star.visibility = View.VISIBLE

                filmCard.setOnLongClickListener {
                    if (item.isFavourite) {
                        File(
                            context.filesDir,
                            context.resources.getString(R.string.fileName, item.kinopoiskId)
                        ).delete()
                        listener.onItemRemoveClick(item.kinopoiskId)
                        star.visibility = View.INVISIBLE
                        item.isFavourite = false
                    } else {
                        saveImageUtil.execute(
                            context.resources.getString(R.string.fileName, item.kinopoiskId),
                            item.posterUrl,
                            context
                        )
                        listener.onItemClick(item.kinopoiskId)
                        star.visibility = View.VISIBLE
                        item.isFavourite = true
                    }
                    true
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DescriptionDTO>() {
        override fun areItemsTheSame(oldItem: DescriptionDTO, newItem: DescriptionDTO): Boolean {
            return oldItem.kinopoiskId == newItem.kinopoiskId
        }

        override fun areContentsTheSame(oldItem: DescriptionDTO, newItem: DescriptionDTO): Boolean {
            return oldItem == newItem
        }
    }
}