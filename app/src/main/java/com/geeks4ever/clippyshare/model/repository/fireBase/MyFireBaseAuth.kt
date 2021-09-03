/*
 * Created by Praveen Kumar for Clippy Share.
 * Copyright (c) 2021.
 * Last modified on 03/09/21, 9:32 PM.
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

import androidx.lifecycle.MutableLiveData
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
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        error.value = task.exception?.message
                    } else currentUser.value = firebaseAuth.currentUser
                    loading.value = false
                }
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun logIn(email: String, password: String) {
        loading.value = true
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        error.value = task.exception?.message
                    } else currentUser.value = firebaseAuth.currentUser
                    loading.value = false
                }
        } catch (e: Exception) {
            error.value = e.message
        }
    }

}