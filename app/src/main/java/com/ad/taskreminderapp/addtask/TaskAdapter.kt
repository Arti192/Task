package com.ad.taskreminderapp.addtask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ad.taskreminderapp.databinding.ItemTaskBinding
import com.ad.taskreminderapp.db.Task

class TaskAdapter(var tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    private lateinit var binding: ItemTaskBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        val binding = holder.binding

        binding.txtTitle.text = task.title
        binding.txtTimeLeft.text = getTimeLeft(task)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    private fun getTimeLeft(task: Task): String {
        val currentTime = System.currentTimeMillis()
        val timeLeftMillis = task.deadline - currentTime

        // Convert time left to a user-friendly format (e.g., hours and minutes)
        val hoursLeft = timeLeftMillis / (1000 * 60 * 60)
        val minutesLeft = (timeLeftMillis % (1000 * 60 * 60)) / (1000 * 60)

        getHours(hoursLeft)
        getMins(minutesLeft)

        return if(getHours(hoursLeft) == "" && getMins(minutesLeft) == ""){
            "Time Left: 0 min"
        }else if(getHours(hoursLeft) == ""){
            "Time Left: " +getMins(minutesLeft)
        } else {
            "Time Left: " + getHours(hoursLeft) + " and" + getMins(minutesLeft)
        }

    }

    private fun getHours(hoursLeft: Long): String {
        return if (hoursLeft.toInt() == 0) {
            ""
        } else if (hoursLeft.toInt() == 1) {
            " $hoursLeft hour"
        } else {
            " $hoursLeft hours"
        }
     }


    private fun getMins(minutesLeft: Long): String {
        return if (minutesLeft.toInt() == 0) {
            ""
        } else if (minutesLeft.toInt() == 1) {
            " $minutesLeft minute"
        } else {
            " $minutesLeft minutes"
        }
    }



class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){


}}
