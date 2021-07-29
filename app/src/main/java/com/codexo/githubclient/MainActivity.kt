package com.codexo.githubclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNav()
    }

    private fun setupBottomNav() {
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myFragmentContainer) as NavHostFragment?
        val navController = navHostFragment!!.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.notificationsFragment,
                R.id.exploreFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)
    }

//    override fun onBackPressed() {
//        Snackbar.make(Context, "Are you sure you want to exit?", Snackbar.LENGTH_LONG)
//            .setAction("Exit!") {
//                super.onBackPressed()
//            }
//            .setBackgroundTint(Color.BLACK)
//            .show()
//    }
}