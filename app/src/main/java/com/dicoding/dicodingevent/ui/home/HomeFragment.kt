package com.dicoding.dicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.dicoding.dicodingevent.ui.adapter.EventFinishedAdapter
import com.dicoding.dicodingevent.ui.adapter.EventUpcomingAdapter
import com.dicoding.dicodingevent.ui.detail.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by viewModels<HomeViewModel>()

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

        val layoutManagerUpcoming = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvEventUpcoming.layoutManager = layoutManagerUpcoming
        val layoutManagerFinished = LinearLayoutManager(requireActivity())
        binding.rvEventFinished.layoutManager = layoutManagerFinished


        homeViewModel.listEventUpcoming.observe(viewLifecycleOwner) { eventListUpcoming ->
            setEventDataUpcoming(eventListUpcoming)
        }

        homeViewModel.listEventFinished.observe(viewLifecycleOwner) { eventListFinished ->
            setEventDataFinished(eventListFinished)
        }
        homeViewModel.errorMessage.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { errorMessage ->
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        return root
    }

    private fun setEventDataUpcoming(listEvent: List<ListEventsItem>) {
        val adapter = EventFinishedAdapter(onItemClick = { eventId -> navigateToDetail(eventId)})
        adapter.submitList(listEvent.take(5))
        binding.rvEventUpcoming.adapter = adapter
    }

    private fun setEventDataFinished(listEvent: List<ListEventsItem>) {
        val adapter = EventUpcomingAdapter(onItemClick = { eventId -> navigateToDetail(eventId) })
        adapter.submitList(listEvent.take(5))
        binding.rvEventFinished.adapter = adapter
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