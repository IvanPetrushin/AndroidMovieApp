package ru.fintech.presentation.fragments.filmcard

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
class FilmCardViewModel @Inject constructor(private val appRepository: AppRepository) :
    ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    var filmId: Int = 0
        set(value) {
            field = value
            loadState()
        }

    fun onRetryButtonPressed() {
        loadState()
    }

    private fun loadState() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val description = appRepository.checkId(filmId)
                _state.value =
                    if (description == null)
                        State.Loaded(appRepository.getDescription(filmId), false)
                    else
                        State.Loaded(description, true)
            } catch (exception: Exception) {
                _state.value = State.Error
            }

        }
    }

    sealed interface State {
        data object Loading : State
        data class Loaded(val description: DescriptionDTO, val isDb: Boolean) : State
        data object Error : State
    }
}