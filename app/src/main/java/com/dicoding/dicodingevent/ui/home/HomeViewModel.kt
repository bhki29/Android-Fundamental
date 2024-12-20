package com.dicoding.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.Result
import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.util.EventWrapper

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _listEventUpcoming = MutableLiveData<List<EventEntity>>()
    val listEventUpcoming: LiveData<List<EventEntity>> = _listEventUpcoming

    private val _listEventFinished = MutableLiveData<List<EventEntity>>()
    val listEventFinished: LiveData<List<EventEntity>> = _listEventFinished

    private val _isLoadingUpcoming = MutableLiveData<Boolean>()
    val isLoadingUpcoming: LiveData<Boolean> = _isLoadingUpcoming

    private val _isLoadingFinished = MutableLiveData<Boolean>()
    val isLoadingFinished: LiveData<Boolean> = _isLoadingFinished


    private val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>> = _errorMessage

    init {
        fetchEventUpcoming()
        fetchEventFinished()
    }

    private fun fetchEventUpcoming() {
        _isLoadingUpcoming.value = true

        eventRepository.fetchUpcomingEvents().observeForever { result ->
            when (result) {
                is Result.Loading -> _isLoadingUpcoming.value = true
                is Result.Success -> {
                    _isLoadingUpcoming.value = false
                    _listEventUpcoming.value = result.data
                }

                is Result.Error -> {
                    _isLoadingUpcoming.value = false
                    _errorMessage.value = EventWrapper("Error: ${result.error}")
                }
            }
        }
    }



    private fun fetchEventFinished() {
        _isLoadingFinished.value = true

        eventRepository.fetchFinishedEvents().observeForever { result ->
            when (result) {
                is Result.Loading -> _isLoadingFinished.value = true
                is Result.Success -> {
                    _isLoadingFinished.value = false
                    _listEventFinished.value = result.data
                }

                is Result.Error -> {
                    _isLoadingFinished.value = false
                    _errorMessage.value = EventWrapper("Error: ${result.error}")
                }
            }
        }
    }

    suspend fun saveEvent(event: EventEntity) {
        eventRepository.setEventFavorite(event, true)
    }

    suspend fun deleteEvent(event: EventEntity) {
        eventRepository.setEventFavorite(event, false)
    }

}