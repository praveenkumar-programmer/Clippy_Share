package com.geeks4ever.clippyshare.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.geeks4ever.clippyshare.R
import com.geeks4ever.clippyshare.viewModel.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.login.*

class Login : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        supportActionBar?.title = "Login"


        findViewById<View>(R.id.login_screen_login_button).setOnClickListener { login() }
        findViewById<View>(R.id.login_screen_signup_button).setOnClickListener { signUp() }

        viewModel = ViewModelProvider(
            this,
            AndroidViewModelFactory(this.application)
        )[AuthViewModel::class.java]
        viewModel.getCurrentUser().observe(this,
            { firebaseUser -> if (firebaseUser != null) gotoHome() })

        viewModel.getLoadingStatus().observe(this,
            Observer { aBoolean ->
                if (aBoolean == null || aBoolean) {
                    login_screen_progress.visibility = View.VISIBLE
                } else {
                    login_screen_progress.visibility = View.INVISIBLE
                }
            })

    }


    private fun login() {

        if (login_screen_email_edit_text.text != null &&
            login_screen_password_edit_text.text != null)

                viewModel.login(
                    login_screen_email_edit_text.text.toString(),
                    login_screen_password_edit_text.text.toString()
                )

        viewModel.getErrorStatus().observeForever { s ->
            if (s != null) {
                Snackbar.make(login_screen_root_layout, s, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun signUp() {
        startActivity(Intent(this, SignUp::class.java))
        finish()
    }

    private fun gotoHome() {
        startActivity(Intent(this, HomePage::class.java))
        finish()
    }



}