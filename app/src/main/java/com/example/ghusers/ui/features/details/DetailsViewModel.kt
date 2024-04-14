package com.example.ghusers.ui.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ghusers.model.Repository
import com.example.ghusers.model.UserDetail
import com.example.ghusers.repository.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(private val service: UserService) : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()
    init {

        _state.update { currentState ->
            currentState.copy(
                fetchUser = {
                    load()
                    fetchUser(it)
                }
            )
        }
    }

    private fun load() {
        _state.value = _state.value.copy(isLoading = true, showEmptyRepository = false)
    }

    private fun fetchUser(login: String) {
        viewModelScope.launch {
            service.getUserWith(login).collect { user ->
                user?.let {
                    _state.value = _state.value.copy(isLoading = false, user = it)
                    fetchRepos(login)
                }
            }
        }
    }

    private fun fetchRepos(name: String) {
        load()
        viewModelScope.launch {
            service.getRepos(name).collect { items ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    repos = items,
                    showEmptyRepository = items.isNullOrEmpty()
                )
            }
        }
    }

    data class State(
        val user: UserDetail? = null,
        val repos: List<Repository>? = null,
        val isLoading : Boolean = false,
        val showEmptyRepository : Boolean = false,
        val fetchUser: (String) -> Unit = {}
    )

}