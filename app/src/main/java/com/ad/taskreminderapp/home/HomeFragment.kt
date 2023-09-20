package com.ad.taskreminderapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ad.taskreminderapp.addtask.TaskAdapter
import com.ad.taskreminderapp.databinding.FragmentHomeBinding
import com.ad.taskreminderapp.db.TaskDatabase
import com.ad.taskreminderapp.db.TaskRepository

class HomeFragment : Fragment() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvTask.layoutManager = LinearLayoutManager(context)
        taskAdapter = TaskAdapter(emptyList())
        binding.rvTask.adapter = taskAdapter

        val repository = TaskRepository(TaskDatabase.getDatabase(requireContext()).taskDao())
        val homeViewModel = ViewModelProvider(this, HomeViewModelFactory(repository))[HomeViewModel::class.java]

        homeViewModel.upcomingTasks.observe(viewLifecycleOwner, Observer {
            taskAdapter.tasks = it
            taskAdapter.notifyDataSetChanged()
        })

        binding.floatingAdd.setOnClickListener {
            (activity as HomeActivity).navigateToCreateTaskFragment()
        }

    }

}