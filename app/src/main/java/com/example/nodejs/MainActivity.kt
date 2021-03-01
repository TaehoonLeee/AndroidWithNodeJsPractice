package com.example.nodejs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
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

        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.friendsFragment -> {}
                R.id.chatRoomFragment -> {}
                R.id.settingFragment -> {}
                R.id.addFriendFragment -> {}
                else -> isShowBar(false)
            }
        }
    }

    fun getUserName() : String = userName

    fun setUserName(_userName : String) {
        userName = _userName
    }

    fun getToken() : String = token

    fun setToken(_token : String) {
        token = _token
    }

    companion object {
        private var userName = ""
        private var token = ""
    }
}