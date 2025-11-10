package com.example.foodapp.screens.Profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentProfileInfoScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileInfoScreen : Fragment() {
    private  var _binding: FragmentProfileInfoScreenBinding? =null
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileInfoScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (uid != null) {
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        val email = document.getString("email")
                        val photoUrl = document.getString("photoUrl")

                        binding.profileName.text = name
                        binding.profileEmail.text = email

                        Glide.with(this)
                            .load(photoUrl)
                            .into(binding.profileImage)
                    } else {
                        Log.d("Profile", "No such document")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Profile", "Error fetching user", e)
                }
        }
    }
}