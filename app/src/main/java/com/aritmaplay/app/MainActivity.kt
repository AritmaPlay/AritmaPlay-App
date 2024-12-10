package com.aritmaplay.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aritmaplay.app.databinding.ActivityMainBinding
import com.aritmaplay.app.ui.onboarding.OnBoardingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSystemBar()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                Log.d("MainActivity", "User is NOT logged in")
                startActivity(Intent(this, OnBoardingActivity::class.java))
                finish()
            } else {
                Log.d("MainActivity", "User is logged in: ${user.token}")
            }
        }

        val navView: BottomNavigationView = binding.navView

        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_rank, R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.historyFragment, R.id.quizFragment, R.id.resultFragment -> {
                    binding.navView.visibility = View.GONE
                    supportActionBar?.show()
                }
                else -> {
                    binding.navView.visibility = View.VISIBLE
                    supportActionBar?.show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupSystemBar() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightNavigationBars = true
            isAppearanceLightStatusBars = true
            window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.yellow_300)
        }
    }
}