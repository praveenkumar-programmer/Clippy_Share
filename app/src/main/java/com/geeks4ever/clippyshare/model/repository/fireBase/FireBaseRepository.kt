/*
 * Created by Praveen Kumar for Clippy Share.
 * Copyright (c) 2021.
 * Last modified on 03/09/21, 9:31 PM.
 *
 * This file/part of Clippy Share is OpenSource.
 *
 * Clippy Share is a free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Clippy Share is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Clippy Share.
 * If not, see http://www.gnu.org/licenses/.
 */

package com.geeks4ever.clippyshare.model.repository.fireBase

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.geeks4ever.clippyshare.model.UrlModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.util.*

object FireBaseRepository {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    val loading = MyFireBaseAuth.loading
    val error = MyFireBaseAuth.error
    val fireBaseUser = MyFireBaseAuth.currentUser
    val urlRecyclerOptions = MutableLiveData<FirebaseRecyclerOptions<UrlModel>> ()
    var uid = "user"


    init {

        try {

            firebaseDatabase.setPersistenceEnabled(true)
            firebaseDatabase.reference.child(uid).keepSynced(true)
            fireBaseUser.observeForever {
                it?.let { refreshUser(it) } }

        } catch (e: Exception) {
            error.value = e.message
        }

    }

    private fun refreshUser(user : FirebaseUser){

        uid = user.uid

        firebaseDatabase.reference.child(uid).keepSynced(true)
        urlRecyclerOptions.value = FirebaseRecyclerOptions.Builder<UrlModel>()
            .setQuery(
                firebaseDatabase.reference.child(uid)
            ) { snapshot ->
                UrlModel(
                    snapshot.child("url").value as String,
                    snapshot.child("dateAndTime").value as String
                )
            }
            .build()

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