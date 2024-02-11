package ru.fintech.entities.dto

data class DescriptionDTO(
    val kinopoiskId: Int,
    val year: String,
    val nameRu: String,
    val posterUrl: String,
    val genres: List<Genre>,
    val countries: List<Country>,
    val description: String,
    var isFavourite: Boolean = false
)
