package com.geeks4ever.clippyshare.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.geeks4ever.clippyshare.R
import com.geeks4ever.clippyshare.model.UrlModel
import com.geeks4ever.clippyshare.view.dialog.AddUrlDialog
import com.geeks4ever.clippyshare.view.viewHolder.UrlViewHolder
import com.geeks4ever.clippyshare.viewModel.HomeViewModel
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.home_page.*

class HomePage : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: FirebaseRecyclerAdapter<UrlModel, UrlViewHolder>

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

        viewModel.getCurrentUser().observeForever { firebaseUser ->
            if (firebaseUser == null) {
                gotoLogin()
                finish()
            }
        }

        viewModel.getLoadingStatus().observe(this,
            { aBoolean ->
                if (aBoolean) {
                    home_screen_progress.visibility = View.VISIBLE
                } else {
                    home_screen_progress.visibility = View.INVISIBLE
                }
            })

        viewModel.getErrorStatus().observeForever { s ->
            if (s != null) {
                if (home_screen_status_text.visibility != View.VISIBLE) home_screen_status_text.visibility =
                    View.VISIBLE
                home_screen_status_text.text = s
                Handler().postDelayed({
                    home_screen_status_text.text = ""
                    if (home_screen_status_text.visibility == View.VISIBLE) home_screen_status_text.visibility =
                        View.GONE
                }, 4000)
            }
        }

        home_screen_add_url_button.setOnClickListener { addUrlToFirebase("") }

        adapter = viewModel.adapter
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
        adapter.startListening()
    }

    override fun onPause() {
        super.onPause()
        adapter.startListening()
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
    }


}