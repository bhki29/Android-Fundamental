package com.dicoding.dicodingevent.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.di.Injection

class SearchModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: SearchModelFactory? = null
        fun getInstance(context: Context): SearchModelFactory =
            instance ?: synchronized(this) {
                instance ?: SearchModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}