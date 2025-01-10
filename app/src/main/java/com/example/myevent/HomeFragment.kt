package com.example.myevent

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myevent.api.ApiConfig
import com.example.myevent.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingEventAdapter: EventAdapter
    private lateinit var finishedEventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerViews()
        fetchEvents()
        return binding.root
    }

    private fun setupRecyclerViews() {
        binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvents.setHasFixedSize(true)
        upcomingEventAdapter = EventAdapter()
        binding.rvUpcomingEvents.adapter = upcomingEventAdapter

        // Finished events in vertical layout
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvFinishedEvents.setHasFixedSize(true)
        finishedEventAdapter = EventAdapter()
        binding.rvFinishedEvents.adapter = finishedEventAdapter
    }

    private fun fetchEvents() {

        if (!isInternetAvailable()) {
            showError("No internet connection ")
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.rvUpcomingEvents.visibility = View.GONE
        binding.rvFinishedEvents.visibility = View.GONE

        val apiService = ApiConfig.getApiService()

        // Fetch upcoming events
        apiService.getUpcomingEvents(1).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents?.take(5)
                    upcomingEventAdapter.submitList(events)
                } else {
                    showError("Failed to get upcoming events")
                }
                binding.rvUpcomingEvents.visibility = View.VISIBLE
                checkIfLoadingComplete()
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showError("Error occurred: ${t.message}")
                checkIfLoadingComplete()
            }
        })

        // Fetch finished events
        apiService.getFinishedEvents(0).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents?.take(5)
                    finishedEventAdapter.submitList(events)
                }else {
                    showError("Failed to get finished events")
                }
                binding.rvFinishedEvents.visibility = View.VISIBLE
                checkIfLoadingComplete()
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showError("Error occurred: ${t.message}")
                checkIfLoadingComplete()
            }
        })
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun checkIfLoadingComplete() {
        if (binding.rvUpcomingEvents.visibility == View.VISIBLE && binding.rvFinishedEvents.visibility == View.VISIBLE) {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}