package com.ad.taskreminderapp.db

import androidx.lifecycle.LiveData

class TaskRepository (private val taskDao: TaskDao) {

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun insertTask(task: Task) {
        taskDao.insert(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.update(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

    fun getUpcomingTasks(): LiveData<List<Task>> {
        val currentTime = System.currentTimeMillis()
        return taskDao.getUpcomingTasks(currentTime)
    }
}