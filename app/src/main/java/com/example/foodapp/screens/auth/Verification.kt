package com.example.foodapp.screens.auth

import android.media.Image
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R


class Verification : Fragment() {

        private lateinit var backbtn: ImageView
        private lateinit var editText: EditText
        private lateinit var editText2: EditText
        private lateinit var editText3: EditText
        private lateinit var editText4: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backbtn = view.findViewById(R.id.back_btn)
        editText = view.findViewById(R.id.emailedittext1)
        editText2 = view.findViewById(R.id.emailedittext2)
        editText3 = view.findViewById(R.id.emailedittext3)
        editText4 = view.findViewById(R.id.emailedittext4)
        setupOtpInputs()

        backbtn.setOnClickListener()
        {
            findNavController().navigate(R.id.action_verification_to_forgotPassword,null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.verification,true)
                    .build())
        }
    }

    private fun setupOtpInputs() {
        val editTexts = listOf(editText, editText2, editText3, editText4)

        for (i in editTexts.indices) {
            val editText = editTexts[i]

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < editTexts.size - 1) {
                        editTexts[i + 1].requestFocus()
                    }
                }
            })

            editText.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (editText.text.isEmpty() && i > 0) {
                        val prev = editTexts[i - 1]
                        prev.requestFocus()
                        prev.text?.clear()
                        return@setOnKeyListener true
                    }
                }
                false
            }
        }
    }



}