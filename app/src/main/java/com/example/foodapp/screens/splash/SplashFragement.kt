package com.example.foodapp.screens.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R

class SplashFragement : Fragment() {

    private lateinit var orangeEllipse:ImageView
    private lateinit var grayEllipse:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_fragement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orangeEllipse = view.findViewById(R.id.orangeellipse)
        grayEllipse = view.findViewById(R.id.grayellipse)

        orangeEllipse.translationY= 600f
        grayEllipse.translationY= -300f


        orangeEllipse.postDelayed({
            orangeEllipse.animate()
                .translationY(0f)
                .setDuration(1000)
                .start()
        },500)


        grayEllipse.postDelayed({
            grayEllipse.animate()
                .translationY(0f)
                .setDuration(1000)
                .start()
        },500)
        Handler(Looper.getMainLooper()).postDelayed({
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.splashFragement, true)
                .build()

            findNavController().navigate(R.id.action_splashFragement_to_login,
                null,
                navOptions
            )
        }, 2000)
    }

}