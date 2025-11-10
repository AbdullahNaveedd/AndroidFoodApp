package com.example.foodapp.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class Home : Fragment() {

    private lateinit var  add_item:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_item = view.findViewById(R.id.add_item)
        add_item.setOnClickListener()
        {
            findNavController().navigate(R.id.action_home3_to_add_item2)

        }
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomnav)
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.homeframe) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.profile3 -> {
                    bottomNav.visibility = View.GONE
                    add_item.visibility = View.GONE
                }
                else -> {
                    bottomNav.visibility = View.VISIBLE
                    add_item.visibility = View.VISIBLE
                }

            }
        }

    }


}