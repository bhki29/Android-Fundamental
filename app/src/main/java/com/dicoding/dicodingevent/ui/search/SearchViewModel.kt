package com.dicoding.dicodingevent.ui.search

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

class SearchViewModel: ViewModel() {

    private val _listEvent = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>> = _errorMessage

    fun fetchSearchEvent(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventSearch(query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                val responseBody = response.body()
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEvent.value = responseBody?.listEvents
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