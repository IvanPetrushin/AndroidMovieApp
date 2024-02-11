package ru.fintech.entities.dto

data class FilmDTO(
    val filmId: Int,
    val nameRu: String,
    val posterUrlPreview: String,
    val year: String,
    val genres: List<Genre>,
    var isFavourite: Boolean = false
)
