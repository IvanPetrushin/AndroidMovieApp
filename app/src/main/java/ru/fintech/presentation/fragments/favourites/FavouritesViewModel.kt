package ru.fintech.presentation.fragments.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.fintech.entities.dto.DescriptionDTO
import ru.fintech.repository.AppRepository
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val appRepository: AppRepository): ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    init {
        loadState()
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

    private fun loadState() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val films = appRepository.getFavourites()
                _state.value = State.Loaded(films)
            } catch (exception: Exception) {
                _state.value = State.Error
            }
        }
    }

    sealed interface State {
        data object Loading : State
        data class Loaded(val films: List<DescriptionDTO>) : State
        data object Error : State
    }
}