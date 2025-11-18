package com.example.foodapp.screens.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class Profile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backbtn.setOnClickListener {
            findNavController().popBackStack()
        }

        val calendarImage = binding.calendaricon
        calendarImage.setOnClickListener {
            val mainNavController = requireActivity().findNavController(R.id.nav_host_fragment)
            mainNavController.navigate(R.id.calendar)
        }

        val recyclerView = binding.profileRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val profileItems = listOf(
            Profile_dataClass(R.drawable.profile, "Personal Info"),
            Profile_dataClass(R.drawable.setting, "Settings"),
            Profile_dataClass(R.drawable.number_of_order, "Number of Orders"),
            Profile_dataClass(R.drawable.withdrawhistory, "WithDraw History"),
            Profile_dataClass(R.drawable.faq, "User Reviews"),
            Profile_dataClass(R.drawable.download, "Log Out")
        )

        val adapter = Profile_Adapter(profileItems){
            item ->
            if(item.title=="Log Out")
            {
                FirebaseAuth.getInstance().signOut()
                val prefs = requireActivity().getSharedPreferences("LoginPrefs", 0)
                prefs.edit().clear().apply()

                requireActivity().findNavController(R.id.nav_host_fragment)
                    .navigate(
                        R.id.login,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.nav_graph, true)
                            .build()
                    )
            }
                if (item.title == "Personal Info") {
                   requireActivity().findNavController(R.id.nav_host_fragment).
                   navigate(R.id.profileInfoScreen)
                }
                if(item.title == "Settings")
                {
                requireActivity().findNavController(R.id.nav_host_fragment).navigate(R.id.settingScreen)
                }


        }
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
