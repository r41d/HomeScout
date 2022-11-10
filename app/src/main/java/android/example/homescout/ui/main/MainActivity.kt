package android.example.homescout.ui.main

import android.content.Intent
import android.example.homescout.R
import android.example.homescout.databinding.ActivityMainBinding
import android.example.homescout.utils.Constants.ACTION_SHOW_NOTIFICATIONS_FRAGMENT
import android.example.homescout.utils.Constants.ACTION_SHOW_SETTINGS_FRAGMENT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // PROPERTIES
    private lateinit var binding: ActivityMainBinding

    // LIFECYCLE FUNCTIONS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_welcome,
                R.id.navigation_notifications,
                R.id.navigation_settings,
                R.id.navigation_scan
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navigateToFragmentIfNeeded(intent)

    }

    override fun onNewIntent(intent: Intent?) {
        navigateToFragmentIfNeeded(intent)
        super.onNewIntent(intent)
    }


    private fun navigateToFragmentIfNeeded(intent: Intent?) {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        when (intent?.action) {
            ACTION_SHOW_SETTINGS_FRAGMENT -> {
                navController.navigate(R.id.action_global_settings_fragment)
            }

            ACTION_SHOW_NOTIFICATIONS_FRAGMENT -> {
                navController.navigate(R.id.action_global_notifications_fragment)
            }

        }
    }

}