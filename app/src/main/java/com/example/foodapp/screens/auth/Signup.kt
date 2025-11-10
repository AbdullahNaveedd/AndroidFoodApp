    package com.example.foodapp.screens.auth
    
    import android.media.Image
    import android.os.Bundle
    import android.text.InputType
    import android.util.Log
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Button
    import android.widget.EditText
    import android.widget.ImageView
    import android.widget.ProgressBar
    import android.widget.Toast
    import androidx.navigation.fragment.findNavController
    import com.example.foodapp.R
    import com.example.foodapp.screens.Constant.Constant
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.firestore.FirebaseFirestore
    
    
    class Signup : Fragment() {
    
            private lateinit var Sign: Button
            private lateinit var showpass: ImageView
            private lateinit var reshowpass: ImageView
            private lateinit var nameeditText: EditText
            private lateinit var emaileditText: EditText
            private lateinit var passedittext: EditText
            private lateinit var retypepassedittext: EditText
            private lateinit var progressBar: ProgressBar
            private lateinit var Backbtn: ImageView

            private lateinit var auth: FirebaseAuth
            private lateinit var db: FirebaseFirestore
    
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
    
            }
        }
    
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_signup, container, false)
        }
    
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
    
            auth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()
    
            Sign = view.findViewById(R.id.signupbtn)
            showpass = view.findViewById(R.id.showpassword)
            reshowpass = view.findViewById(R.id.retypepassimage)
            nameeditText = view.findViewById(R.id.nameedittext)
            emaileditText = view.findViewById(R.id.emailedittext)
            passedittext = view.findViewById(R.id.passwordedittext)
            retypepassedittext = view.findViewById(R.id.retypepassedit)
            progressBar = view.findViewById(R.id.progressbar)
            Backbtn =view.findViewById(R.id.back_btn)


            progressBar.visibility=View.GONE

            Backbtn.setOnClickListener()
            {
                findNavController().navigate(R.id.action_signup_to_login)
            }
    
            Sign.setOnClickListener {
                val name = nameeditText.text.toString().trim()
                val email = emaileditText.text.toString().trim()
                val password = passedittext.text.toString().trim()
                val confirmPass = retypepassedittext.text.toString().trim()

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(requireContext(), "${Constant.allFIelds}", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                if (password != confirmPass) {
                    Toast.makeText(
                        requireContext(),
                        "${Constant.passworddonotmatch}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                progressBar.visibility = View.VISIBLE

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid

                            val userMap = hashMapOf(
                                Constant.name to name,
                                Constant.email to email
                            )

                            if (userId != null) {
                                db.collection("users").document(userId)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        progressBar.visibility = View.GONE
                                        context?.let {
                                            Toast.makeText(
                                                it,
                                                Constant.signupSuccessful,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        if (isAdded && view != null) {
                                            findNavController().navigate(
                                                R.id.action_signup_to_login,
                                                null,
                                                androidx.navigation.NavOptions.Builder()
                                                    .setPopUpTo(R.id.signup, true)
                                                    .build()
                                            )
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        progressBar.visibility = View.GONE
                                        context?.let {
                                            Toast.makeText(
                                                it,
                                                "Firestore Error: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                        } else {
                            progressBar.visibility = View.GONE
                            context?.let {
                                Toast.makeText(
                                    it,
                                    "Auth Failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Log.e("SignupError", "Auth failed", task.exception)
                        }
                    }

                var isPasswordVisible = false

                showpass.setOnClickListener {
                    if (isPasswordVisible) {
                        passedittext.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        isPasswordVisible = false
                    } else {
                        passedittext.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        isPasswordVisible = true
                    }

                    passedittext.setSelection(passedittext.text.length)
                }


                reshowpass.setOnClickListener {
                    if (isPasswordVisible) {
                        retypepassedittext.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        isPasswordVisible = false
                    } else {
                        retypepassedittext.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        isPasswordVisible = true
                    }

                    retypepassedittext.setSelection(retypepassedittext.text.length)
                }


            }
        }
    }