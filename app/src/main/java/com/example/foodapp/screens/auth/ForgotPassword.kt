package com.example.foodapp.screens.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R

class ForgotPassword : Fragment() {

    private lateinit var backbtn: ImageView
    private lateinit var sendbtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backbtn = view.findViewById(R.id.back_btn)
        sendbtn = view.findViewById(R.id.sendbtn)
        backbtn.setOnClickListener()
        {
            findNavController().navigate(R.id.action_forgotPassword_to_login,null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.forgotPassword,true)
                    .build())
        }

        sendbtn.setOnClickListener()
        {
            findNavController().navigate(R.id.action_forgotPassword_to_verification,null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.forgotPassword,true)
                    .build())
        }

    }
}