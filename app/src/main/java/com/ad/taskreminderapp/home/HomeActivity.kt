package com.ad.taskreminderapp.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ad.taskreminderapp.addtask.CreateTaskFragment
import com.ad.taskreminderapp.R
import com.ad.taskreminderapp.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, HomeFragment::class.java.simpleName)
            .commit()

    }

    fun navigateToCreateTaskFragment() {
        val fragment = CreateTaskFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, CreateTaskFragment::class.java.simpleName)
            .addToBackStack(null) // Optional: Allows going back to the previous fragment
            .commit()
    }
}