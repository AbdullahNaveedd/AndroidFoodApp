package com.example.foodapp.screens.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.foodapp.databinding.FragmentSettingScreenBinding

class SettingScreen : Fragment() {

    private var _binding: FragmentSettingScreenBinding? = null
    private val binding get() = _binding!!
    private val counter: Counter by viewModels()
    private val playerViewModel: PlayerViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        counter.number.observe(viewLifecycleOwner) { value ->
            binding.text.text = value.toString()


            binding.increment.setOnClickListener {
                counter.increment()
            }
            binding.decrement.setOnClickListener {
                if ((counter.number.value ?: 0) > 0) {
                    counter.decrement()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Number is Equal to Zero No Decremnt",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

    }

    override fun onStart() {
        super.onStart()
        playerViewModel.initializePlayer(requireContext())
        binding.playerView.player = playerViewModel.player
    }

    override fun onResume() {
        super.onResume()
        playerViewModel.player?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.saveState()
        playerViewModel.pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        binding.playerView.player = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}