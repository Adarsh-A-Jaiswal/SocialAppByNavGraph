package com.example.socialappbynavgraph.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialappbynavgraph.apiService.Api
import com.example.socialappbynavgraph.apiService.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ListViewModel : ViewModel() {

    val listData: MutableLiveData<List<Users>> = MutableLiveData()
    val deleteResponse: MutableLiveData<Int> = MutableLiveData()
    val postData: MutableLiveData<Response<Users>> = MutableLiveData()
    val putData: MutableLiveData<Response<Users>> = MutableLiveData()

    fun getDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = Api.getRetrofitInstance().getUsersDetails()
            if (response.isNotEmpty()) {
                listData.postValue(response)
            } else {
                Log.e(TAG, "Error to load data from server")
            }
        }
    }

    fun deleteProfile(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = Api.getRetrofitInstance().deleteProfile(userId)
            if (response.isSuccessful) {
                deleteResponse.postValue(response.code())
            } else {
                Log.e(TAG, "deleteProfile: error to delete profile")
            }
        }
    }

    fun postData(post: Users) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = Api.getRetrofitInstance().postProfile(post)
            if (response.isSuccessful) {
                postData.postValue(response)
            } else {
                Log.e(TAG, "postData: error to post Data")
            }
        }
    }

    fun putData(userId: Int, post: Users) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = Api.getRetrofitInstance().putProfile(userId, post)
            if (response.isSuccessful) {
                putData.postValue(response)
            } else {
                Log.e(TAG, "putData: error to put Data")
            }
        }
    }

}
