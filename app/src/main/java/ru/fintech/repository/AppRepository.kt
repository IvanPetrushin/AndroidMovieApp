package ru.fintech.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
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

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getPopular(pageCount: Int) = apiService.getPopular(page = pageCount)

    suspend fun getDescription(id: Int): DescriptionDTO = apiService.getDescription(id =  id)

    suspend fun getFavourites(): List<DescriptionDTO> =
        filmInfoDao.getFavourites().map { it.description }

    suspend fun checkId(kinopoiskId: Int): DescriptionDTO? =
        filmInfoDao.checkId(kinopoiskId)?.description

    suspend fun insertFavourite(description: DescriptionDTO) {
        filmInfoDao.insertFavourite(FilmInfo(description.kinopoiskId, description))
        saveToFirebase(description)
    }

    suspend fun deleteFavourite(kinopoiskId: Int) {
        filmInfoDao.deleteFavourite(kinopoiskId)
        deleteFromFirebase(kinopoiskId)
    }

    private fun saveToFirebase(description: DescriptionDTO) {
        val filmMap = mapOf(
            "id" to description.kinopoiskId,
            "name" to description.nameRu,
            "description" to description.description,
            "posterUrl" to description.posterUrl,
            "isFavourite" to true
        )
        try {
            firestore.collection("favourites")
                .document(description.kinopoiskId.toString())
                .set(filmMap)
                .addOnSuccessListener {
                    Log.d("Firebase_SAVE", "Saved to Firebase: ${description.kinopoiskId}")
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase_SAVE", "Failed to save to Firebase", e)
                }
        } catch (e: Exception) {
            Log.e("Firebase_SAVE", e.message.toString())
        }
    }

    private fun deleteFromFirebase(kinopoiskId: Int) {
        try {
            firestore.collection("favourites")
                .document(kinopoiskId.toString())
                .delete()
                .addOnSuccessListener {
                    Log.d("Firebase_SAVE", "Deleted from Firebase: $kinopoiskId")
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase_SAVE", "Failed to delete from Firebase", e)
                }
        } catch (e: Exception) {
            Log.e("Firebase_SAVE", e.message.toString())
        }
    }
}