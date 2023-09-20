package com.ad.taskreminderapp.addtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ad.taskreminderapp.db.Task
import com.ad.taskreminderapp.db.TaskRepository

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val taskUpcomingData : LiveData<List<Task>> = repository.getUpcomingTasks()

    fun isTaskDataValid(title: String, description: String): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }

    suspend fun addTask(title: String, description: String, deadline: Long) {
        val newTask = Task(0,title, description, deadline)
        repository.insertTask(newTask)
    }
}