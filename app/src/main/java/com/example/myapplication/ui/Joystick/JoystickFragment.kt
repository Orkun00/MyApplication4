package com.example.myapplication.ui.Joystick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentJoystickBinding

class JoystickFragment : Fragment() {

    private var _binding: FragmentJoystickBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJoystickBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the VirtualJoystickView
        val joystick = binding.virtualJoystick
        joystick.setOnMoveListener { x, y ->
            // Log or process joystick x and y values
            println("Joystick moved: x=$x, y=$y")

            // Handle x, y data (e.g., send to ROS or other system)
            sendJoystickData(x, y)
        }

        return root
    }

    private fun sendJoystickData(x: Float, y: Float) {
        // Handle joystick x, y data (e.g., send to a server or ROS system)
        println("Sending joystick data: x=$x, y=$y")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
