package com.example.myevent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myevent.databinding.FragmentFavoriteBinding

class FragmentFavorite : Fragment() {
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var eventRepository: EventRepository
    private lateinit var favoriteAdapter: FavoriteEventAdapter
    private lateinit var binding: FragmentFavoriteBinding

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentFavoriteBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val database = EventDatabase.getInstance(requireContext())
            eventRepository = EventRepository(database.favoriteEventDao())
            val factory = FavoriteViewModelFactory(eventRepository)
            favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

            setupRecyclerView()

            binding.progressBarFavorite.visibility = View.VISIBLE
            binding.tvNoFavorites.visibility = View.GONE

            favoriteViewModel.favoriteEvents.observe(viewLifecycleOwner) { favorites ->
                binding.progressBarFavorite.visibility = View.GONE

                if (favorites.isEmpty()) {
                    binding.tvNoFavorites.visibility = View.VISIBLE
                } else {
                    binding.tvNoFavorites.visibility = View.GONE
                }
                favoriteAdapter.submitList(favorites)
            }
        }

        private fun setupRecyclerView() {
            favoriteAdapter = FavoriteEventAdapter(
                onRemoveClick = { event ->
                    favoriteViewModel.removeFavorite(event)
                },
                onItemClick = { event ->
                    val intent = Intent(requireContext(), ActivityDetail::class.java).apply {
                        putExtra("id", event.id)
                    }
                    startActivity(intent)
                }
            )
            binding.recyclerViewFavorite.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = favoriteAdapter
            }
        }
    }
