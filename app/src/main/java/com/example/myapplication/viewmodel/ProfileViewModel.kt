package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).dao()

    val userProfile = dao.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun saveProfile(name: String, phone: String, email: String) {
        viewModelScope.launch {
            val updatedUser = userProfile.value?.copy(name = name, phoneNumber = phone, email = email) 
                ?: User(name = name, phoneNumber = phone, email = email)
            dao.updateUserProfile(updatedUser)
        }
    }
}