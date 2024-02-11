package ru.fintech.entities.dto

data class ResponseDTO(
    val films: List<FilmDTO>,
    val pagesCount: Int
)