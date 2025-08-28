package com.example.thegroomly.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.example.thegroomly.data.Repository

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repository(app)
    val groomers = repo.groomersFlow.asLiveData()
    val favorites = repo.favoritesFlow.asLiveData()   // <--- bez zagrada, to je property
}
