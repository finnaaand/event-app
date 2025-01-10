package com.example.myevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myevent.api.ApiConfig
import com.example.myevent.databinding.FragmentFinishedBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentFinished : Fragment() {

    private lateinit var binding: FragmentFinishedBinding
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishedBinding.inflate(inflater, container, false)
        adapter = EventAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.progressBarFinished.visibility = View.VISIBLE
        fetchFinishedEvents()

        return binding.root
    }

    private fun fetchFinishedEvents() {
        val apiService = ApiConfig.getApiService()
        apiService.getFinishedEvents(0).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val finishedEvents = response.body()?.listEvents ?: emptyList()
                    adapter.submitList(finishedEvents)
                    binding.progressBarFinished.visibility = View.GONE
                } else {
                    binding.progressBarFinished.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                binding.progressBarFinished.visibility = View.GONE
            }
        })
    }
}

