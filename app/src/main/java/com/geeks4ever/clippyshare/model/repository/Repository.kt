/*
 * Created by Praveen Kumar for Clippy Share.
 * Copyright (c) 2021.
 * Last modified on 03/09/21, 9:30 PM.
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

package com.geeks4ever.clippyshare.model.repository

import com.geeks4ever.clippyshare.model.repository.fireBase.FireBaseRepository

object Repository {

    private val database = FireBaseRepository

    val fireBaseUser = database.fireBaseUser
    val loading = database.loading
    val error = database.error
    val urlRecyclerOptions = database.urlRecyclerOptions

    fun addUrl(url: String, dateAndTime: String) { database.addUrl(url,dateAndTime) }

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