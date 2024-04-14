package com.example.ghusers.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ghusers.model.UserItem
import com.example.ghusers.repository.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val service: UserService) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    init {
        fetchData()
        _state.update { currentState ->
            currentState.copy(
                onRefresh = {
                    load()
                    fetchData()
                },
                onSearch = {
                    load()
                    onSearchWith(it)
                },
                onRetry = {
                    load()
                    fetchData()
                }
            )
        }
    }

    private fun load() {
        _state.value = _state.value.copy(isLoading = true, showError = false)
    }

    private fun fetchData() {
        viewModelScope.launch {
            service.getUsers().collect {
                _state.value = _state.value.copy(isLoading = false, items = it, showError = it.isEmpty())
            }
        }
    }

    private fun onSearchWith(value: String) {
        viewModelScope.launch {
            service.getUserWith(name = value).collect { user ->
                val newUserItem = user?.let {
                    UserItem(
                        id = it.id,
                        login = it.login,
                        avatarUrl = it.avatar
                    )
                }
                _state.value = _state.value.copy(
                    isLoading = false,
                    items = if (newUserItem != null) listOf(newUserItem) else emptyList(),
                    showError = newUserItem == null
                )
            }
        }
    }

    data class State(
        val isLoading: Boolean = true,
        val text: String = "",
        val items: List<UserItem> = emptyList(),
        val showError: Boolean = false,
        val onRefresh: () -> Unit = {},
        val onRetry: () -> Unit = {},
        val onSearch: (String) -> Unit = {}
    ) {
        fun isFirstLoading() = isLoading && items.isEmpty()
    }
}