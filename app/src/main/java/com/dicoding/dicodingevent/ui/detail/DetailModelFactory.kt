package com.dicoding.dicodingevent.ui.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.di.Injection


class DetailModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailModelFactory? = null
        fun getInstance(context: Context): DetailModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }

}