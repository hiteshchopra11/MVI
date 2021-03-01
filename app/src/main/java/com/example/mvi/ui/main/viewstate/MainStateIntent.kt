package com.example.mvi.ui.main.viewstate

import com.example.mvi.data.model.User

sealed class MainStateIntent {
    object Idle : MainStateIntent()
    object Loading : MainStateIntent()
    data class Users(val user: List<User>) : MainStateIntent()
    data class Error(val error: String?) : MainStateIntent()
}