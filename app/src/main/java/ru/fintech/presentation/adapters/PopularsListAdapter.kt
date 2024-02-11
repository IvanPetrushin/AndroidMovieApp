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
import coil.load
import ru.fintech.R
import ru.fintech.databinding.FilmItemBinding
import ru.fintech.entities.dto.FilmDTO
import ru.fintech.presentation.fragments.filmcard.FilmCardFragment
import ru.fintech.utils.SaveImageUtil
import java.io.File

class PopularsListAdapter :
    ListAdapter<FilmDTO, PopularsListAdapter.ItemViewHolder>(DiffCallback()) {

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

    class ItemViewHolder(private val binding: FilmItemBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        private val saveImageUtil: SaveImageUtil = SaveImageUtil()
        fun bind(item: FilmDTO, listener: RecyclerClickListener) {
            with(binding) {
                itemImage.load(
                    item.posterUrlPreview.toUri().buildUpon().scheme("https").build()
                )
                itemName.text = item.nameRu
                itemGenreYear.text = context.getString(
                    R.string.genreYear,
                    item.genres[0].genre.replaceFirstChar { it.uppercase() },
                    item.year
                )
                star.visibility = if (item.isFavourite) View.VISIBLE else View.INVISIBLE

                filmCard.setOnClickListener { view ->
                    view.findNavController().navigate(
                        R.id.action_popularsFragment_to_filmCardFragment,
                        FilmCardFragment.createArguments(filmId = item.filmId)
                    )
                }
                filmCard.setOnLongClickListener {
                    val isFavourite = star.visibility == View.VISIBLE
                    if (isFavourite) {
                        File(
                            context.filesDir,
                            context.resources.getString(R.string.fileName, item.filmId)
                        ).delete()
                        listener.onItemRemoveClick(item.filmId)
                        star.visibility = View.INVISIBLE
                    } else {
                        saveImageUtil.execute(
                            context.resources.getString(R.string.fileName, item.filmId),
                            item.posterUrlPreview,
                            context
                        )
                        listener.onItemClick(item.filmId)
                        star.visibility = View.VISIBLE
                    }
                    true
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FilmDTO>() {
        override fun areItemsTheSame(oldItem: FilmDTO, newItem: FilmDTO): Boolean {
            return oldItem.nameRu == newItem.nameRu
        }

        override fun areContentsTheSame(oldItem: FilmDTO, newItem: FilmDTO): Boolean {
            return oldItem == newItem
        }
    }
}