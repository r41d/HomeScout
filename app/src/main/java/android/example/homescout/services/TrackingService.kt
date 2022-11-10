package android.example.homescout.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.example.homescout.R
import android.example.homescout.repositories.TrackingPreferencesRepository
import android.example.homescout.ui.main.MainActivity
import android.example.homescout.utils.Constants.ACTION_SHOW_SETTINGS_FRAGMENT
import android.example.homescout.utils.Constants.ACTION_START_SERVICE
import android.example.homescout.utils.Constants.ACTION_STOP_SERVICE
import android.example.homescout.utils.Constants.CHANNEL_ID_TRACKING_PROTECTION
import android.example.homescout.utils.Constants.NOTIFICATION_CHANNEL_NAME
import android.example.homescout.utils.Constants.NOTIFICATION_ID
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TrackingService () : LifecycleService() {

    // TODO: replace this with switch from settingsViewModel or TrackingRepository
    @Inject
    lateinit var trackingPreferencesRepository : TrackingPreferencesRepository


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {

                ACTION_START_SERVICE -> {
                    Timber.i("Start Service")
                    startForegroundService()
                }

                ACTION_STOP_SERVICE -> {
                    Timber.i("Stop Service") }
                }
            }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        createNotificationChannel(notificationManager)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID_TRACKING_PROTECTION)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_notifications_24px)
            .setContentTitle("Home Scout")
            .setContentText("Tracking service is running.")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_SETTINGS_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID_TRACKING_PROTECTION,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}