package ru.fintech.repository

import ru.fintech.entities.dao.FilmInfo
import ru.fintech.entities.dao.FilmInfoDAO
import ru.fintech.entities.dto.DescriptionDTO
import ru.fintech.services.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val apiService: ApiService,
    private val filmInfoDao: FilmInfoDAO,
) {
    suspend fun getPopular(pageCount: Int) = apiService.getPopular(page = pageCount)

    suspend fun getDescription(id: Int): DescriptionDTO = apiService.getDescription(id =  id)

    suspend fun getFavourites(): List<DescriptionDTO> =
        filmInfoDao.getFavourites().map { it.description }

    suspend fun checkId(kinopoiskId: Int): DescriptionDTO? =
        filmInfoDao.checkId(kinopoiskId)?.description

    suspend fun insertFavourite(description: DescriptionDTO) =
        filmInfoDao.insertFavourite(FilmInfo(description.kinopoiskId, description))

    suspend fun deleteFavourite(kinopoiskId: Int) =
        filmInfoDao.deleteFavourite(kinopoiskId)
}