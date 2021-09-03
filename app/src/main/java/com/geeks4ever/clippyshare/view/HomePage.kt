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

package com.geeks4ever.clippyshare.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.geeks4ever.clippyshare.R
import com.geeks4ever.clippyshare.model.UrlModel
import com.geeks4ever.clippyshare.view.dialog.AddUrlDialog
import com.geeks4ever.clippyshare.view.viewHolder.UrlViewHolder
import com.geeks4ever.clippyshare.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.home_page.*

class HomePage : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private var adapter: FirebaseRecyclerAdapter<UrlModel, UrlViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        supportActionBar?.title = "Home"

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        home_screen_posts_recycler_view.layoutManager = layoutManager

        viewModel = ViewModelProvider(
            this,
            AndroidViewModelFactory(this.application)
        )[HomeViewModel::class.java]

        viewModel.fireBaseUser.observeForever { firebaseUser ->
            if (firebaseUser == null) {
                gotoLogin()
            }
        }

        viewModel.loading.observe(this,
            { aBoolean ->
                if (aBoolean) {
                    home_screen_progress.visibility = View.VISIBLE
                } else {
                    home_screen_progress.visibility = View.INVISIBLE
                }
            })

        viewModel.error.observeForever { s ->
            if (s != null) {
                if (home_screen_status_text.visibility != View.VISIBLE) home_screen_status_text.visibility =
                    View.VISIBLE
                home_screen_status_text.text = s
                Handler(Looper.getMainLooper()).postDelayed({
                    home_screen_status_text.text = ""
                    if (home_screen_status_text.visibility == View.VISIBLE) home_screen_status_text.visibility =
                        View.GONE
                }, 4000)
            }
        }

        home_screen_add_url_button.setOnClickListener { addUrlToFirebase("") }

        viewModel.adapter.observeForever{

            it?.let{

                adapter = it
            }


        }

        home_screen_posts_recycler_view.adapter = adapter

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                        addUrlToFirebase(it)
                    }
                }
            }
        }


    }

    private fun addUrlToFirebase(url : String) {

        AddUrlDialog.newInstance(url).show(supportFragmentManager,"Add URL")

    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onPause() {
        super.onPause()
        adapter?.startListening()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.menu_item_logout) {
            logout()
            return true
        } else if (itemId == R.id.menu_item_exit) {
            exit()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exit() {
        finish()
    }

    private fun logout() {
        viewModel.logout()
    }

    private fun gotoLogin() {
        startActivity(Intent(this, Login::class.java))
        finish()
    }


}