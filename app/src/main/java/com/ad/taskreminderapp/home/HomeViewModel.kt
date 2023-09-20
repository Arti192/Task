package com.ad.taskreminderapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ad.taskreminderapp.db.Task
import com.ad.taskreminderapp.db.TaskRepository

class HomeViewModel(private val repository: TaskRepository) : ViewModel() {

    val upcomingTasks: LiveData<List<Task>> = repository.getUpcomingTasks()
}