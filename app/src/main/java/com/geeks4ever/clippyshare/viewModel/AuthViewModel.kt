package com.geeks4ever.clippyshare.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.geeks4ever.clippyshare.model.repository.Repository
import com.geeks4ever.clippyshare.model.repository.fireBase.MyFireBaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.core.Repo

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val fireBaseUser = Repository.fireBaseUser
    private val loading = Repository.loading
    private val error = Repository.error


    fun getErrorStatus(): LiveData<String?> {
        return error
    }

    fun getLoadingStatus(): LiveData<Boolean?> {
        return loading
    }

    fun getCurrentUser(): LiveData<FirebaseUser> {
        return fireBaseUser
    }

    fun login(email: String, password: String) {
        Repository.logIn(email, password)
    }

    fun signUp(email: String, password: String) {
        Repository.signUp(email, password)
    }


}