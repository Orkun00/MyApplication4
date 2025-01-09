package com.example.myapplication.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentHomeBinding
import okhttp3.*
import org.json.JSONObject

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var webSocket: WebSocket

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Connect to ROS bridge and subscribe to camera topic
        connectToRosBridge()
        subscribeToCameraFeed()

        return root
    }

    private fun connectToRosBridge() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("ws://10.200.40.97:9090") // Use 10.0.2.2 if using Android Emulator
            .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("ROS", "Connected to ROS bridge")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                handleIncomingMessage(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("ROS", "WebSocket connection failed", t)
            }
        })
    }

    private fun subscribeToCameraFeed() {
        val message = JSONObject().apply {
            put("op", "subscribe")
            put("topic", "/usb_cam0/image_raw/compressed")
            put("type", "sensor_msgs/msg/CompressedImage")
        }
        webSocket.send(message.toString())
    }


    private fun handleIncomingMessage(message: String) {
        try {
            val json = JSONObject(message)
            if (json.optString("topic") == "/usb_cam0/image_raw/compressed") {
                val msg = json.getJSONObject("msg")
                val imageData = Base64.decode(msg.getString("data"), Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)

                activity?.runOnUiThread {
                    val safeBinding = _binding ?: return@runOnUiThread // Exit if binding is null
                    safeBinding.imageView.setImageBitmap(bitmap)
                }
            }
        } catch (e: Exception) {
            Log.e("HomeFragment", "Error handling compressed image message", e)
        }
    }




    private fun convertRGBToBitmap(data: ByteArray, width: Int, height: Int): Bitmap {
        val pixels = IntArray(width * height)
        for (i in pixels.indices) {
            val r = data[i * 3].toInt() and 0xFF
            val g = data[i * 3 + 1].toInt() and 0xFF
            val b = data[i * 3 + 2].toInt() and 0xFF
            pixels[i] = (0xFF shl 24) or (r shl 16) or (g shl 8) or b
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
    }


    private fun convertBGRToBitmap(data: ByteArray, width: Int, height: Int): Bitmap {
        val pixels = IntArray(width * height)
        for (i in pixels.indices) {
            val b = data[i * 3].toInt() and 0xFF
            val g = data[i * 3 + 1].toInt() and 0xFF
            val r = data[i * 3 + 2].toInt() and 0xFF
            pixels[i] = (0xFF shl 24) or (r shl 16) or (g shl 8) or b
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent access to binding

    }

}
