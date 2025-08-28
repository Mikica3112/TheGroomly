package com.example.thegroomly.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class UserUi(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = ""
)

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = app.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    private val _user = MutableLiveData<UserUi>(loadFromPrefs())
    val user: LiveData<UserUi> = _user

    private fun loadFromPrefs(): UserUi = UserUi(
        name = prefs.getString("name", "") ?: "",
        email = prefs.getString("email", "") ?: "",
        phone = prefs.getString("phone", "") ?: "",
        address = prefs.getString("address", "") ?: ""
    )

    fun saveUser(u: UserUi) {
        prefs.edit()
            .putString("name", u.name)
            .putString("email", u.email)
            .putString("phone", u.phone)
            .putString("address", u.address)
            .apply()
        _user.value = u
    }
}
