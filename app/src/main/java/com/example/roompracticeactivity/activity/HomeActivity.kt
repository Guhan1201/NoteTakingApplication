package com.example.roompracticeactivity.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.roompracticeactivity.R

class HomeActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navController = Navigation.findNavController(
            this,
            R.id.hostFragment
        )
    }
}