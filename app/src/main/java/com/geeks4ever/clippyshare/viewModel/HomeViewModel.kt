package com.geeks4ever.clippyshare.viewModel

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.geeks4ever.clippyshare.R
import com.geeks4ever.clippyshare.model.UrlModel
import com.geeks4ever.clippyshare.model.repository.Repository
import com.geeks4ever.clippyshare.view.viewHolder.UrlViewHolder
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val loading = Repository.loading
    private val error = Repository.error
    private val fireBaseUser = Repository.fireBaseUser

    var adapter: FirebaseRecyclerAdapter<UrlModel, UrlViewHolder> = object :
        FirebaseRecyclerAdapter<UrlModel, UrlViewHolder>(Repository.getUrlRecyclerOptions()) {

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
            holder.root.setOnClickListener(View.OnClickListener {

                val clipboard: ClipboardManager = application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("url", model.url)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(application.applicationContext,
                    "Link Copied to Clipboard",
                    Toast.LENGTH_SHORT
                ).show()

            })
        }

    }


    fun getLoadingStatus(): LiveData<Boolean> {
        return loading
    }

    fun getErrorStatus(): LiveData<String> {
        return error
    }

    fun getCurrentUser(): LiveData<FirebaseUser> {
        return fireBaseUser
    }

    fun logout() {
        Repository.logOut()
    }

    fun addPost(url: String, dateAndTime: String) {
        Repository.addUrl(url, dateAndTime)
    }

}