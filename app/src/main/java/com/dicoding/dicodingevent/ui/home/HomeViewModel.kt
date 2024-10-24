package com.dicoding.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.remote.response.EventResponse
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.data.remote.retrofit.ApiConfig
import com.dicoding.dicodingevent.util.EventWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _listEventUpcoming = MutableLiveData<List<ListEventsItem>>()
    val listEventUpcoming: LiveData<List<ListEventsItem>> = _listEventUpcoming

    private val _listEventFinished = MutableLiveData<List<ListEventsItem>>()
    val listEventFinished: LiveData<List<ListEventsItem>> = _listEventFinished

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>> = _errorMessage

    init {
        findEventUpComing()
        findFinishedComing()
    }

    private fun findEventUpComing() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventUpcoming()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                val responseBody = response.body()
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEventUpcoming.value = responseBody?.listEvents
                } else {
                    _errorMessage.value = EventWrapper(response.body()?.message.toString())
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = EventWrapper("Network Error : ${t.message.toString()}")
            }
        })
    }

    private fun findFinishedComing() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventFinished()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                val responseBody = response.body()
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEventFinished.value = responseBody?.listEvents
                } else {
                    _errorMessage.value = EventWrapper(response.body()?.message.toString())
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = EventWrapper("Network Error : ${t.message.toString()}")
            }
        })
    }
}