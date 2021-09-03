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

package com.geeks4ever.clippyshare.viewModel


import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.geeks4ever.clippyshare.R
import com.geeks4ever.clippyshare.model.UrlModel
import com.geeks4ever.clippyshare.model.repository.Repository
import com.geeks4ever.clippyshare.view.viewHolder.UrlViewHolder


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val loading = Repository.loading
    val error = Repository.error
    val fireBaseUser = Repository.fireBaseUser
    val adapter = MutableLiveData<FirebaseRecyclerAdapter<UrlModel, UrlViewHolder>> ()

    init {

        Repository.urlRecyclerOptions.observeForever {
            it?.let{

                adapter.value = object :
                    FirebaseRecyclerAdapter<UrlModel, UrlViewHolder>(it) {

                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrlViewHolder {
                        val view: View = LayoutInflater.from(parent.context)
                            .inflate(R.layout.url_item, parent, false)
                        return UrlViewHolder(view)
                    }

                    override fun onBindViewHolder(
                        holder: UrlViewHolder,
                        position: Int,
                        model: UrlModel
                    ) {
                        holder.urlText.text = model.url
                        holder.dateAndTimeText.text = model.dateAndTime
                        holder.root.setOnClickListener {

                            val clipboard: ClipboardManager =
                                application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("url", model.url)
                            clipboard.setPrimaryClip(clip)

                            Toast.makeText(
                                application.applicationContext,
                                "Link Copied to Clipboard",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                }

            }
        }
    }


    fun logout() {
        Repository.logOut()
    }

    fun addPost(url: String, dateAndTime: String) {
        Repository.addUrl(url, dateAndTime)
    }

}