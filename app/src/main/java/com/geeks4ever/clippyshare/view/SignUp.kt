package com.geeks4ever.clippyshare.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.geeks4ever.clippyshare.R
import com.geeks4ever.clippyshare.viewModel.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.sign_up.*

class SignUp : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        supportActionBar?.title = "Sign Up"

        findViewById<View>(R.id.sign_up_screen_sign_up_button).setOnClickListener { signup() }

        viewModel = ViewModelProvider(
            this,
            AndroidViewModelFactory(this.application)
        )[AuthViewModel::class.java]
        viewModel.getCurrentUser().observe(this,
            { firebaseUser -> if (firebaseUser != null) gotoHome() })

        viewModel.getLoadingStatus().observe(this,
            Observer { aBoolean ->
                if (aBoolean == null || aBoolean) {
                    sign_up_screen_progress.visibility = View.VISIBLE
                } else {
                    sign_up_screen_progress.visibility = View.INVISIBLE
                }
            })


    }


    private fun signup() {
        if (sign_up_screen_email_edit_text.text != null &&
            sign_up_screen_password_edit_text.text != null &&
            sign_up_screen_confirm_password_edit_text.text != null) {

            val email = sign_up_screen_email_edit_text.text.toString()
            val password = sign_up_screen_password_edit_text.text.toString()
            val confirmPassword = sign_up_screen_confirm_password_edit_text.text.toString()
            if (password == confirmPassword) viewModel.signUp(email, password) else Toast.makeText(
                this,
                "Passwords Don't Match!",
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.getErrorStatus().observeForever { s ->
            if (s != null) {
                Snackbar.make(sign_up_screen_root_layout, s, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun gotoHome() {
        startActivity(Intent(this, HomePage::class.java))
        finish()
    }


}