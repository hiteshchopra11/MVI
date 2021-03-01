package com.example.mvi.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvi.data.repository.MainRepository
import com.example.mvi.ui.main.intent.MainIntent
import com.example.mvi.ui.main.viewstate.MainStateIntent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainStateIntent>(MainStateIntent.Idle)
    val state: StateFlow<MainStateIntent> get() = (_state)


    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchUser -> fetchUser()
                }
            }
        }
    }

    private fun fetchUser() {
        viewModelScope.launch {
            _state.value = MainStateIntent.Loading
            _state.value = try {
                MainStateIntent.Users(repository.getUsers())
            } catch (e: Exception) {
                MainStateIntent.Error(e.localizedMessage)
            }
        }
    }
}