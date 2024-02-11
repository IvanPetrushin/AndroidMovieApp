package ru.fintech.presentation.fragments.popular


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.fintech.entities.dto.ResponseDTO
import ru.fintech.repository.AppRepository
import javax.inject.Inject

@HiltViewModel
class PopularsViewModel @Inject constructor(private val appRepository: AppRepository) :
    ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    init {
        loadState()
    }

    fun onRetryButtonPressed(page: Int) {
        loadState(page = page)
    }

    fun onScrolledEnough(page: Int) {
        loadState(
            page = page,
            isLoadInit = false
        )
    }

    fun onItemRemoveClicked(filmId: Int) {
        viewModelScope.launch {
            try {
                appRepository.deleteFavourite(filmId)
            } catch (exception: Exception) {
                _state.value = State.Error
            }
        }
    }

    fun onItemClicked(filmId: Int) {
        viewModelScope.launch {
            try {
                val description = appRepository.getDescription(filmId)
                description.isFavourite = true
                appRepository.insertFavourite(description)
            } catch (exception: Exception) {
                _state.value = State.Error
            }
        }
    }

    private fun loadState(page: Int = 1, isLoadInit: Boolean = true) {
            viewModelScope.launch {
                if (isLoadInit) _state.value = State.Loading
                try {
                    val filmsListAPI = appRepository.getPopular(pageCount = page)
                    val films = filmsListAPI.films

                    val favourites = appRepository.getFavourites()
                    val intersect =
                        films.map { it.filmId }.intersect(favourites.map { it.kinopoiskId }.toSet())
                    films.forEach { film ->
                        if (intersect.contains(film.filmId)) film.isFavourite = true
                    }
                    _state.value = State.Loaded(filmsListAPI)
                } catch (exception: Exception) {
                    _state.value = State.Error
                }
            }
    }

    sealed interface State {
        data object Loading : State
        data class Loaded(val filmsData: ResponseDTO) : State
        data object Error : State
    }
}