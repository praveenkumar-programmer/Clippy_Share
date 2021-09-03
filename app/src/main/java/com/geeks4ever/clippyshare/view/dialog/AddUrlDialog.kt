package com.geeks4ever.clippyshare.view.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.geeks4ever.clippyshare.R
import com.geeks4ever.clippyshare.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_add_url_dialog.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddUrlDialog : DialogFragment() {

    companion object {

        private const val KEY_URL = "KEY_URL"

        fun newInstance(url: String): AddUrlDialog {

            val args = Bundle()
            args.putString(KEY_URL, url)
            val fragment = AddUrlDialog()
            fragment.arguments = args
            return fragment
        }

    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_url_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)

        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {
        view.urlEditText.setText(arguments?.getString(KEY_URL))
    }

    private fun setupClickListeners(view: View) {

        view.btnPositive.setOnClickListener {

            if(view.urlEditText.text.toString() == "")
                Toast.makeText(this.context, "Please type or paste a link!", Toast.LENGTH_SHORT).show()
            else {
                val cal = java.util.Calendar.getInstance()
                val currentLocalTime: Date = cal.time
                val date: DateFormat = SimpleDateFormat("dd-MMM-yyy hh:mm aa")
                val localTime: String = date.format(currentLocalTime)
                viewModel.addPost(view.urlEditText.text.toString(), localTime)
                dismiss()
            }
        }
        view.btnNegative.setOnClickListener {
            dismiss()
        }
    }

}