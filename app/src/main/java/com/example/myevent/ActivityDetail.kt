package com.example.myevent

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myevent.api.ApiConfig
import com.example.myevent.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityDetail : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var eventLink: String? = null
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var event: Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = EventDatabase.getInstance(applicationContext).favoriteEventDao()
        val eventRepository = EventRepository(dao)
        val factory = FavoriteViewModelFactory(eventRepository)
        favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]


        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val eventId = intent.getIntExtra("id", -1)
        if (eventId != -1) {
            getEventDetail(eventId)
            binding.progressBarDetail.visibility = View.VISIBLE
        } else {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.favoriteButton.setOnClickListener {
            event?.let {
                val favoriteEvent = FavoriteEventEntity(
                    id = it.id ?: 0,
                    name = it.name ?: "",
                    mediaCover = it.mediaCover ?: "",
                    beginTime = it.beginTime ?: ""
                )
                favoriteViewModel.addFavorite(favoriteEvent)
                Toast.makeText(this, "${it.name} added to favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getEventDetail(eventId: Int) {

        if (!isInternetAvailable()) {
            showError("No internet connection ")
            return
        }

        val client = ApiConfig.getApiService().getEventDetail(eventId)
        client.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                if (response.isSuccessful) {
                    binding.progressBarDetail.visibility = View.GONE
                    event = response.body()?.event
                    event?.let { displayEventDetail(it) }
                } else {
                    binding.progressBarDetail.visibility = View.GONE
                    showError("Failed to get the event details ")
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                binding.progressBarDetail.visibility = View.GONE
            }
        })
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    private fun displayEventDetail(event: Event) {
        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.eventImage)

        binding.eventName.text = event.name
        binding.eventOrganizer.text = event.ownerName
        binding.eventTime.text = getString(
            R.string.event_time_format, event.beginTime, event.endTime
        )

        val remaining = (event.quota ?: 0) - (event.registrants ?: 0)
        binding.remainingQuota.text = getString(
            R.string.remaining_quota_format, remaining
        )

        binding.eventDescription.text = HtmlCompat.fromHtml(
            event.description ?: "",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        eventLink = event.link
        binding.openLinkButton.setOnClickListener {
            eventLink?.let { link ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
        }
    }
}
