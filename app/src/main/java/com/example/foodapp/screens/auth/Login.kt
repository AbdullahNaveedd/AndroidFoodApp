package com.example.foodapp.screens.auth

import android.content.Context
import android.content.SharedPreferences
import android.media.Image
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class Login : Fragment() {

    private lateinit var Signup: TextView
    private lateinit var Login: Button
    private lateinit var forgotpassword: TextView
    private lateinit var showpassword: ImageView
    private lateinit var passwordedittext: EditText
    private lateinit var emailedittext: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var progressBar: ProgressBar
    private lateinit var checkBox: CheckBox
    private lateinit var sharedPref: SharedPreferences
    private lateinit var google: ImageView
    private lateinit var fb: ImageView
    private lateinit var insta: ImageView
    private lateinit var twitter: ImageView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    private val googleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.result

            if (account == null) {
                Toast.makeText(requireContext(), "Sign-in failed", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }

            val userName = account.displayName ?: ""
            val userEmail = account.email ?: ""
            val idToken = account.idToken
            val photo = account.photoUrl ?: ""

            Log.d("GoogleSignIn", "Name: $userName")
            Log.d("GoogleSignIn", "Email: $userEmail")
            Log.d("GoogleSignIn", "ID Token: $idToken")
            Log.d("GoogleSignIn", "Photo: $photo")

            if (idToken != null) {
                progressBar.visibility = View.VISIBLE

                val credential =
                    com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)

                auth.signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        progressBar.visibility = View.GONE

                        if (authTask.isSuccessful) {
                            val user = auth.currentUser
                            val userId = user?.uid
                            if (userId != null) {
                                val userMap = hashMapOf(
                                    "name" to userName,
                                    "email" to userEmail,
                                    "photoUrl" to (account.photoUrl?.toString() ?: ""),
                                    "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                                )

                                db.collection("users").document(userId)
                                    .set(userMap, com.google.firebase.firestore.SetOptions.merge())
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "Google Sign-In Successful!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val prefs =
                                            requireActivity().getSharedPreferences("LoginPrefs", 0)
                                        prefs.edit().putBoolean("isLoggedIn", true).apply()

                                        if (isAdded) {
                                            findNavController().navigate(
                                                R.id.action_login_to_home3,
                                                null,
                                                androidx.navigation.NavOptions.Builder()
                                                    .setPopUpTo(R.id.login, true)
                                                    .build()
                                            )
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            requireContext(),
                                            "Firestore error: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Firebase Auth Failed: ${authTask.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    "ID Token is null. Check Firebase configuration.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Error: ${e.message}", e)
            progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Signup = view.findViewById(R.id.SignUpTxt)
        Login = view.findViewById(R.id.loginbtn)
        forgotpassword = view.findViewById(R.id.forgetpasstext)
        showpassword = view.findViewById(R.id.showpassword)
        passwordedittext = view.findViewById(R.id.passwordedittext)
        emailedittext = view.findViewById(R.id.emailedittext)
        progressBar = view.findViewById(R.id.progressbar)
        checkBox = view.findViewById(R.id.checkbox)
        google = view.findViewById(R.id.google)
        fb = view.findViewById(R.id.fb)
        insta = view.findViewById(R.id.insta)
        twitter = view.findViewById(R.id.twitter)
        callbackManager = CallbackManager.Factory.create()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val prefs = requireActivity().getSharedPreferences("LoginPrefs", 0)
        val isLoggedIn = prefs.getBoolean("isLoggedIn", false)
        initGoogleLogin()

        if (isLoggedIn && auth.currentUser != null) {
            findNavController().navigate(
                R.id.action_login_to_home3,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.login, true)
                    .build()
            )
        }

        progressBar.visibility = View.GONE

        var isPasswordVisible = false


        showpassword.setOnClickListener {
            if (isPasswordVisible) {
                passwordedittext.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                isPasswordVisible = false
            } else {
                passwordedittext.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                isPasswordVisible = true
            }

            passwordedittext.setSelection(passwordedittext.text.length)
        }


        forgotpassword.setOnClickListener()
        {
            findNavController().navigate(
                R.id.action_login_to_forgotPassword, null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.login, true)
                    .build()
            )
        }


        Signup.setOnClickListener()
        {
            findNavController().navigate(
                R.id.action_login_to_signup, null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.login, true)
                    .build()
            )
        }
        google.setOnClickListener {

            googleSignInClient.signOut().addOnCompleteListener {
                googleLauncher.launch(googleSignInClient.signInIntent)
            }
        }
        insta.setOnClickListener {

        }
        fb.setOnClickListener {
            facebooklogin()
        }
        twitter.setOnClickListener {
            val provider = com.google.firebase.auth.OAuthProvider.newBuilder("twitter.com")

            val pendingResultTask = auth.pendingAuthResult
            if (pendingResultTask != null) {
                pendingResultTask
                    .addOnSuccessListener { authResult ->
                        handleTwitterSignInSuccess(authResult.user)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            "Twitter login failed: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                auth.startActivityForSignInWithProvider(requireActivity(), provider.build())
                    .addOnSuccessListener { authResult ->
                        handleTwitterSignInSuccess(authResult.user)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            "Twitter login failed: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

        Login.setOnClickListener {
            val email = emailedittext.text.toString().trim()
            val password = passwordedittext.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "All fields required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            db.collection("users").document(userId).get()
                                .addOnSuccessListener { document ->
                                    progressBar.visibility = View.GONE
                                    if (document.exists()) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Logged in",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        if (checkBox.isChecked) {
                                            val prefs = requireActivity().getSharedPreferences(
                                                "LoginPrefs",
                                                0
                                            )
                                            prefs.edit().putBoolean("isLoggedIn", true).apply()
                                        }

                                        if (isAdded) {
                                            findNavController().navigate(
                                                R.id.action_login_to_home3, null,
                                                androidx.navigation.NavOptions.Builder()
                                                    .setPopUpTo(R.id.login, true)
                                                    .build()
                                            )
                                        }
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Please register first",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        auth.signOut()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        requireContext(),
                                        "Firestore error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Login Failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun initGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun facebooklogin() {
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            listOf("email", "public_profile")
        )

        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(r: LoginResult) {
                    val request = GraphRequest.newMeRequest(r.accessToken) { obj, _ ->
                        val email = obj?.getString("email")
                        val name = obj?.getString("name")
                        Toast.makeText(requireContext(), "FB: $email $name", Toast.LENGTH_LONG)
                            .show()
                    }
                    val b = Bundle()
                    b.putString("fields", "id,name,email")
                    request.parameters = b
                    request.executeAsync()
                }

                override fun onCancel() {
                    Toast.makeText(requireContext(), "FB Cancel", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(requireContext(), "FB Error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun handleTwitterSignInSuccess(user: com.google.firebase.auth.FirebaseUser?) {
        if (user == null) {
            Toast.makeText(requireContext(), "No user data found", Toast.LENGTH_SHORT).show()
            return
        }

        val userMap = hashMapOf(
            "name" to (user.displayName ?: ""),
            "email" to (user.email ?: ""),
            "photoUrl" to (user.photoUrl?.toString() ?: ""),
            "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
        )

        db.collection("users").document(user.uid)
            .set(userMap, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Twitter Sign-In Successful!", Toast.LENGTH_SHORT)
                    .show()

                val prefs = requireActivity().getSharedPreferences("LoginPrefs", 0)
                prefs.edit().putBoolean("isLoggedIn", true).apply()

                findNavController().navigate(
                    R.id.action_login_to_home3,
                    null,
                    androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.login, true)
                        .build()
                )
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Firestore error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onResume() {
        super.onResume()
        logScreenViewFirebaseEvent()
    }

    private fun logScreenViewFirebaseEvent() {

        val analytics = Firebase.analytics
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, "LoginFragment")
        }
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }


}