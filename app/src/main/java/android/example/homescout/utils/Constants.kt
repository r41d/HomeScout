package android.example.homescout.utils

object Constants {

    // Database
    const val HOMESCOUT_DATABASE_NAME = "homescout_db"
    const val TRACKING_PREFERENCES = "tracking_preferences"

    // Permissions
    const val LOCATION_PERMISSION_REQUEST_CODE = 1

    // Bluetooth
    const val APPLE_COMPANY_IDENTIFIER = 76

    // Notifications for found device
    const val CHANNEL_ID_FOUND_DEVICE = "found_device_channel"
    const val NOTIFICATION_CHANNEL_FOUND_DEVICE = "found_device"
    const val NOTIFICATION_ID_FOUND_DEVICE = 1
    const val ACTION_SHOW_NOTIFICATIONS_FRAGMENT = "ACTION_SHOW_NOTIFICATIONS_FRAGMENT"

    // Notifications for tracking service
    const val CHANNEL_ID_TRACKING_PROTECTION = "tracking_protection_channel"
    const val NOTIFICATION_CHANNEL_TRACKING = "tracking"
    const val NOTIFICATION_ID_TRACKING = 2
    const val ACTION_SHOW_SETTINGS_FRAGMENT = "ACTION_SHOW_SETTINGS_FRAGMENT"

    // Service
    const val ACTION_START_SERVICE = "ACTION_START_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L
}