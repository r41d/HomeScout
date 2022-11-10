package android.example.homescout.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.example.homescout.R
import android.example.homescout.databinding.ActivityMainBinding
import android.example.homescout.utils.Constants.ACTION_SHOW_SETTINGS_FRAGMENT
import android.example.homescout.utils.Constants.CHANNEL_ID_DEVICE_FOUND
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

        navigateToSettingsFragmentIfNeeded(intent)

        createNotificationChannel()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToSettingsFragmentIfNeeded(intent)
    }

    // LIFECYCLE FUNCTIONS
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "test_notifications_channel"
        val descriptionText = "this test the notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID_DEVICE_FOUND, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun navigateToSettingsFragmentIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_SETTINGS_FRAGMENT) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(R.id.action_global_settings_fragment)
        }
    }

}