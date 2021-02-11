package com.example.nodejs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)

        btnGET.setOnClickListener { v ->
            navController.navigate(R.id.getFragment)
        }

        btnPOST.setOnClickListener { v ->
            navController.navigate(R.id.postFragment)
        }
    }
}