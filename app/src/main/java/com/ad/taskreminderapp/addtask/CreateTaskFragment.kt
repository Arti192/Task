package com.ad.taskreminderapp.addtask

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context.*
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ad.taskreminderapp.TaskReminderReceiver
import com.ad.taskreminderapp.databinding.FragmentCreateTaskBinding
import com.ad.taskreminderapp.db.TaskDatabase
import com.ad.taskreminderapp.db.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateTaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateTaskBinding
    private val calendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = TaskRepository(TaskDatabase.getDatabase(requireContext()).taskDao())
        val taskViewModel = ViewModelProvider(this, TaskViewModelFactory(repository))[TaskViewModel::class.java]

        binding.edtDeadline.setOnClickListener {
            showDateTimePicker()
        }

        binding.btnCreateTask.setOnClickListener {
            val title = binding.edtTitle.text.toString()
            val description = binding.edtDescription.text.toString()
            val deadline = calendar.timeInMillis

            if(taskViewModel.isTaskDataValid(title, description)){
                CoroutineScope(Dispatchers.IO).launch {
                    taskViewModel.addTask(title, description, deadline)

                    setReminder(title, deadline)
                    withContext(Dispatchers.Main) {
                        binding.edtTitle.text?.clear()
                        binding.edtDescription.text?.clear()
                        binding.edtDescription.clearFocus()
                        binding.edtDeadline.text?.clear()
                    }
                }
            }else {
                if (title.isEmpty()) {
                    binding.edtTitle.error = "Title is required"
                }
                if (description.isEmpty()) {
                    binding.edtDescription.error = "Description is required"
                }
            }
        }

        taskViewModel.taskUpcomingData.observe(viewLifecycleOwner) {

        }
    }

    private fun setReminder(title: String, deadline: Long) {
        val alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, TaskReminderReceiver::class.java)
        intent.putExtra("taskTitle", title) // Pass necessary data
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = deadline // Set the deadline time in milliseconds

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun showDateTimePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                showTimePicker()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
                val formattedDate = dateFormat.format(calendar.time)
                binding.edtDeadline.setText(formattedDate)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )

        timePickerDialog.show()
    }
}