package com.dicoding.dicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.dicoding.dicodingevent.ui.adapter.EventFinishedAdapter
import com.dicoding.dicodingevent.ui.adapter.EventUpcomingAdapter
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by viewModels<HomeViewModel>{
        HomeModelFactory.getInstance(requireActivity())
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvEventUpcoming.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        binding.rvEventFinished.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
        }


        homeViewModel.listEventUpcoming.observe(viewLifecycleOwner) {eventListUpcoming ->
            setEventDataUpcoming(eventListUpcoming)
        }

        homeViewModel.listEventFinished.observe(viewLifecycleOwner) { eventListFinished ->
            setEventDataFinished(eventListFinished)
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {errorMessage ->
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        homeViewModel.isLoadingUpcoming.observe(viewLifecycleOwner) { isLoadingUpcoming ->
            showLoadingUpcoming(isLoadingUpcoming)
        }

        homeViewModel.isLoadingFinished.observe(viewLifecycleOwner) { isLoadingFinished ->
            showLoadingFinished(isLoadingFinished)
        }


        return root
    }

    private fun setEventDataUpcoming(listEvent: List<EventEntity>) {
        val adapter = EventFinishedAdapter(
            onItemClick = { eventId -> navigateToDetail(eventId)},
            onFavoriteClick = { event ->
                viewLifecycleOwner.lifecycleScope.launch {
                    toggleFavorite(event)
                }
            })
        adapter.submitList(listEvent.take(5))
        binding.rvEventUpcoming.adapter = adapter
    }

    private fun setEventDataFinished(listEvent: List<EventEntity>) {
        val adapter = EventUpcomingAdapter(
            onItemClick = { eventId -> navigateToDetail(eventId) },
            onFavoriteClick = { event ->
                viewLifecycleOwner.lifecycleScope.launch {
                    toggleFavorite(event)
                }
            })
        adapter.submitList(listEvent.take(5))
        binding.rvEventFinished.adapter = adapter
    }

    private fun showLoadingUpcoming(isLoading: Boolean) {
        binding.progressBarUpcoming.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoadingFinished(isLoading: Boolean) {
        binding.progressBarFinished.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private suspend fun toggleFavorite(event: EventEntity) {
        if (event.isFavorite) {
            homeViewModel.deleteEvent(event)
        } else {
            homeViewModel.saveEvent(event)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToDetail(eventId: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("EVENT_ID", eventId)
        }
        startActivity(intent)
    }

}