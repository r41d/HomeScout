package android.example.homescout.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.example.homescout.R
import android.example.homescout.repositories.MainRepository
import android.example.homescout.repositories.TrackingPreferencesRepository
import android.example.homescout.ui.main.MainActivity
import android.example.homescout.utils.Constants.ACTION_SHOW_SETTINGS_FRAGMENT
import android.example.homescout.utils.Constants.ACTION_START_TRACKER_CLASSIFICATION_SERVICE
import android.example.homescout.utils.Constants.ACTION_STOP_TRACKER_CLASSIFICATION_SERVICE
import android.example.homescout.utils.Constants.CHANNEL_ID_TRACKER_CLASSIFICATION
import android.example.homescout.utils.Constants.INTERVAL_TRACKER_CLASSIFICATION
import android.example.homescout.utils.Constants.NOTIFICATION_CHANNEL_TRACKER_CLASSIFICATION
import android.example.homescout.utils.Constants.NOTIFICATION_ID_TRACKER_CLASSIFICATION
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.asLiveData
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TrackerClassificationService : LifecycleService() {


    private val handler = Handler(Looper.getMainLooper())

    private var isServiceRunning = false

    private var distance : Float? = null
    private var time : Float? = null
    private var occurrences : Float? = null

    @Inject
    lateinit var trackingPreferencesRepository: TrackingPreferencesRepository


    override fun onCreate() {
        super.onCreate()

        trackingPreferencesRepository.distance.asLiveData().observe(this) { distance = it }
        trackingPreferencesRepository.time.asLiveData().observe(this) { time = it }
        trackingPreferencesRepository.occurrences.asLiveData().observe(this) {
            occurrences = it
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {

            when (it.action) {

                ACTION_START_TRACKER_CLASSIFICATION_SERVICE -> {
                    if (!isServiceRunning) {
                        Timber.i("Start Service")
                        isServiceRunning = true
                        startForegroundService()
                    }
                }

                ACTION_STOP_TRACKER_CLASSIFICATION_SERVICE -> {
                    Timber.i("Stop Service")
                    isServiceRunning = false
                    stopSelf()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {

        startTrackerClassification()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        createNotificationChannel(notificationManager)

        val notificationBuilder = NotificationCompat.Builder(this,
            CHANNEL_ID_TRACKER_CLASSIFICATION
        )
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_protect_48px)
            .setContentTitle("Home Scout")
            .setContentText("Tracker classification is running.")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID_TRACKER_CLASSIFICATION, notificationBuilder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID_TRACKER_CLASSIFICATION,
            NOTIFICATION_CHANNEL_TRACKER_CLASSIFICATION,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_SETTINGS_FRAGMENT
        },
        PendingIntent.FLAG_IMMUTABLE
    )

    private fun startTrackerClassification() {

        if (isServiceRunning) {



            Timber.i("time: $time")
            Timber.i("distance: $distance")
            Timber.i("occurrences: $occurrences")

            handler.postDelayed({
                startTrackerClassification()
            }, INTERVAL_TRACKER_CLASSIFICATION)
        }
    }
}