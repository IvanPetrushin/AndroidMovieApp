package ru.fintech.presentation.adapters

interface RecyclerClickListener {
    fun onItemRemoveClick(filmId: Int)
    fun onItemClick(filmId: Int)
}