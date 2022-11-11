package android.example.homescout.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.example.homescout.R
import android.example.homescout.ui.main.MainActivity
import android.example.homescout.utils.Constants.ACTION_SHOW_SETTINGS_FRAGMENT
import android.example.homescout.utils.Constants.ACTION_START_SERVICE
import android.example.homescout.utils.Constants.ACTION_STOP_SERVICE
import android.example.homescout.utils.Constants.CHANNEL_ID_TRACKING_PROTECTION
import android.example.homescout.utils.Constants.FASTEST_LOCATION_INTERVAL
import android.example.homescout.utils.Constants.LOCATION_UPDATE_INTERVAL
import android.example.homescout.utils.Constants.NOTIFICATION_CHANNEL_TRACKING
import android.example.homescout.utils.Constants.NOTIFICATION_ID_TRACKING
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


typealias Coordinates = MutableList<LatLng>

@AndroidEntryPoint
class TrackingService () : LifecycleService() {

    var isServiceRunning = false

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        // mutableLiveData needed?
        val userPositions = MutableLiveData<Coordinates>()
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {

                ACTION_START_SERVICE -> {
                    if (!isServiceRunning) {
                        Timber.i("Start Service")
                        startForegroundService()
                        isServiceRunning = true
                    }
                }

                ACTION_STOP_SERVICE -> {
                    Timber.i("Stop Service")
                    isServiceRunning = false
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                    stopSelf()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    // FUNCTIONS FOR TO START SERVICE
    private fun startForegroundService() {

        postInitialValues()

        updateLocationTracking()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        createNotificationChannel(notificationManager)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID_TRACKING_PROTECTION)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_protect_48px)
            .setContentTitle("Home Scout")
            .setContentText("Tracking service is running.")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID_TRACKING, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_SETTINGS_FRAGMENT
        },
        FLAG_IMMUTABLE
    )

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID_TRACKING_PROTECTION,
            NOTIFICATION_CHANNEL_TRACKING,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    // FUNCTIONS FOR LOCATION TRACKING
    private fun postInitialValues() {
        userPositions.postValue(mutableListOf())
    }

    private fun addPosition(location: Location?) {
        location?.let {
            val currentPosition = LatLng(location.latitude, location.longitude)
            userPositions.value?.apply {
                add(currentPosition)
                userPositions.postValue(this) // in case MutableLiveData is displayed somewhere
            }
        }
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result.locations.let { locations ->
                for (location in locations){
                    Timber.i("NEW LOCATION: lat: ${location.latitude}, lng: ${location.longitude}")
                    addPosition(location)
                }
            }
        }
    }

    private fun updateLocationTracking() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }


        val locationRequest = LocationRequest.Builder(
            PRIORITY_HIGH_ACCURACY,
            LOCATION_UPDATE_INTERVAL
        ).build()


        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

    }

}