package com.geeks4ever.clippyshare.model.repository.fireBase

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


object MyFireBaseAuth {

    var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser = MutableLiveData<FirebaseUser>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    init {

        try {
            currentUser.value = firebaseAuth.currentUser
        } catch (e: Exception) {
            error.value = e.message
        }
        loading.value = false

    }

    fun logOut() {
        loading.value = true
        try {
            firebaseAuth.signOut()
        } catch (e: Exception) {
            error.value = e.message
        }
        currentUser.value = null
        loading.value = false
    }

    fun signUp(email: String, password: String) {
        loading.value = true
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                    if (!task.isSuccessful) {
                        error.value = task.exception?.message
                        Log.e(TAG, "logIn: ", task.exception)
                    } else currentUser.value = firebaseAuth.currentUser
                    loading.value = false
                })
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun logIn(email: String, password: String) {
        loading.value = true
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                    if (!task.isSuccessful) {
                        error.value = task.exception?.message
                        Log.e(TAG, "logIn: ", task.exception)
                    } else currentUser.value = firebaseAuth.currentUser
                    loading.value = false
                })
        } catch (e: Exception) {
            error.value = e.message
        }
    }

}