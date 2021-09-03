package com.geeks4ever.clippyshare.model.repository

import androidx.lifecycle.LiveData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.geeks4ever.clippyshare.model.UrlModel
import com.geeks4ever.clippyshare.model.repository.fireBase.FireBaseRepository

object Repository {

    private val database = FireBaseRepository

    val fireBaseUser = database.fireBaseUser
    val loading = database.loading
    val error = database.error

    fun addUrl(url: String, dateAndTime: String) { database.addUrl(url,dateAndTime) }


    fun getUrlRecyclerOptions(): FirebaseRecyclerOptions<UrlModel> {
        return database.urlRecyclerOptions
    }

    fun logIn(email : String, password : String){
        database.logIn(email, password)
    }

    fun signUp(email : String, password : String){
        database.signUp(email, password)
    }

    fun logOut(){
        database.logOut()
    }


}