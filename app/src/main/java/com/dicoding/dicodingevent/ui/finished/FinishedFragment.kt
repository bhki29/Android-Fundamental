package com.dicoding.dicodingevent.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.databinding.FragmentFinishedBinding
import com.dicoding.dicodingevent.ui.adapter.EventFinishedAdapter
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import kotlinx.coroutines.launch

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val finishedViewModel by viewModels<FinishedViewModel>{
        FinishedModelFactory.getInstance(requireActivity())
    }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvEvent.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }

        finishedViewModel.listEvent.observe(viewLifecycleOwner) {eventList ->
            setEventDataFinished(eventList)
        }

        finishedViewModel.errorMessage.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {errorMessage ->
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) {loading ->
            showLoading(loading)
        }

        return root

    }

    private fun setEventDataFinished(listEvent: List<EventEntity>) {
        val adapter = EventFinishedAdapter(
            onItemClick = { eventId -> navigateToDetail(eventId)},
            onFavoriteClick = { event ->
                viewLifecycleOwner.lifecycleScope.launch {
                    toggleFavorite(event)
                }
            })
        adapter.submitList(listEvent)
        binding.rvEvent.adapter = adapter
    }

    private suspend fun toggleFavorite(event: EventEntity) {
        if (event.isFavorite) {
            finishedViewModel.deleteEvent(event)
        } else {
            finishedViewModel.saveEvent(event)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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