package com.example.nodejs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpNavigation()
    }

    fun isShowBar(show : Boolean) {
        when(show) {
            true -> {
                lineview.visibility = View.VISIBLE
                navigation_bar.visibility = View.VISIBLE
            }
            false -> {
                lineview.visibility = View.GONE
                navigation_bar.visibility = View.GONE
            }
        }
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.navigation_bar).setupWithNavController(navController)
    }

    fun getUserName() : String = userName

    fun setUserName(_userName : String) {
        userName = _userName
    }

    companion object {
        private var userName = ""
    }
}