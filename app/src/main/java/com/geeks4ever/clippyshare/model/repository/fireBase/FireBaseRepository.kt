package com.geeks4ever.clippyshare.model.repository.fireBase

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.geeks4ever.clippyshare.model.UrlModel
import com.google.firebase.database.FirebaseDatabase
import java.util.*

object FireBaseRepository {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    val loading = MyFireBaseAuth.loading
    val error = MyFireBaseAuth.error
    val fireBaseUser = MyFireBaseAuth.currentUser

    var uid = "user";

    lateinit var urlRecyclerOptions: FirebaseRecyclerOptions<UrlModel>


    init {

        try {
            fireBaseUser.value?.let { uid = it.uid}
            firebaseDatabase.setPersistenceEnabled(true)
            firebaseDatabase.reference.child(uid).keepSynced(true)

            urlRecyclerOptions = FirebaseRecyclerOptions.Builder<UrlModel>()
                .setQuery(
                    firebaseDatabase.reference.child(uid)
                ) { snapshot ->
                    UrlModel(
                        snapshot.child("url").value as String,
                        snapshot.child("dateAndTime").value as String
                    )
                }
                .build()
        } catch (e: Exception) {
            error.value = e.message
        }

    }

    fun addUrl(url: String, dateAndTime: String) {

        loading.value = true
        try {
            val databaseReference = firebaseDatabase.reference.child(uid).push()
            val map: MutableMap<String, Any?> = HashMap()
            map["url"] = url
            map["dateAndTime"] = dateAndTime
            databaseReference.setValue(map).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    error.value = task.exception?.message
                    Log.e(ContentValues.TAG, "logIn: ", task.exception)
                }
                loading.value = false
            }
        } catch (e: Exception) {
            error.value = e.message
        }
        loading.value = false
    }


    fun logIn(email : String, password : String){
        MyFireBaseAuth.logIn(email, password)
    }

    fun signUp(email : String, password : String){
        MyFireBaseAuth.signUp(email, password)
    }

    fun logOut(){
        MyFireBaseAuth.logOut()
    }



}