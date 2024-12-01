package com.example.myapplication.ui.Joystick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.FragmentJoystickBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JoystickFragment : Fragment() {

    private var _binding: FragmentJoystickBinding? = null
    private val binding get() = _binding!!

    // LiveData for joystick coordinates
    private val _joystickCoordinates = MutableLiveData<Pair<Float, Float>>()
    val joystickCoordinates: LiveData<Pair<Float, Float>> = _joystickCoordinates

    // Coroutine job to handle delayed updates
    private var updateJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJoystickBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up the VirtualJoystickView
        val joystick = binding.virtualJoystick
        joystick.setOnMoveListener { x, y ->
            updateJoystickData(x, y)
        }

        // Observe LiveData to update UI
        joystickCoordinates.observe(viewLifecycleOwner) { coordinates ->
            val (x, y) = coordinates
            binding.textCoordinates.text = "X: ${"%.2f".format(x)}, Y: ${"%.2f".format(y)}"
        }

        return root
    }

    private fun updateJoystickData(x: Float, y: Float) {
        // Cancel any existing job
        updateJob?.cancel()

        // Start a new coroutine to delay updates
        updateJob = lifecycleScope.launch {
             // Add a delay of 100ms
            _joystickCoordinates.postValue(Pair(x, y))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        updateJob?.cancel() // Cancel the coroutine job to avoid memory leaks
    }
}
