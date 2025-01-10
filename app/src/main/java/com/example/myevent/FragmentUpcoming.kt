package com.example.myevent

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myevent.api.ApiConfig
import com.example.myevent.databinding.FragmentUpcomingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentUpcoming : Fragment() {

    private lateinit var binding: FragmentUpcomingBinding
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = EventAdapter()
        binding.recyclerView.adapter = adapter
        binding.progressBarUpcoming.visibility = View.VISIBLE
        fetchUpcomingEvents()

        return binding.root
    }
    private fun fetchUpcomingEvents() {

        if (!isInternetAvailable()) {
            binding.progressBarUpcoming.visibility = View.GONE
            Toast.makeText(requireContext(), "No internet connection ", Toast.LENGTH_SHORT).show()
            return
        }

        val apiService = ApiConfig.getApiService()
        apiService.getUpcomingEvents(1).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val upcomingEvents = response.body()?.listEvents ?: emptyList()
                    adapter.submitList(upcomingEvents)
                    binding.progressBarUpcoming.visibility = View.GONE
                    binding.recyclerView.adapter = adapter
                } else {
                    binding.progressBarUpcoming.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                binding.progressBarUpcoming.visibility = View.GONE
                Toast.makeText(requireContext(), "Failed to load events: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
