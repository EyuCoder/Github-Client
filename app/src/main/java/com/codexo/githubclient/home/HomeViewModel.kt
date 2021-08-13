package com.codexo.githubclient.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class HomeViewModel(state : SavedStateHandle): ViewModel() {

    // Keep the key as a constant
    companion object {
        private const val USER_KEY = "userId"
    }

    private val savedStateHandle = state

    fun saveCurrentUser(userId: String) {
        // Sets a new value for the object associated to the key.
        savedStateHandle.set(USER_KEY, userId)
    }

    fun getCurrentUser(): String {
        // Gets the current value of the user id from the saved state handle
        return savedStateHandle.get(USER_KEY)?: ""
    }

}