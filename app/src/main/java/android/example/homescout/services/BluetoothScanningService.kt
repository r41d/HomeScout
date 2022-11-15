package android.example.homescout.services
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.example.homescout.R
import android.example.homescout.ui.main.MainActivity
import android.example.homescout.utils.BluetoothAPILogger
import android.example.homescout.utils.Constants
import android.example.homescout.utils.Constants.ACTION_SHOW_SETTINGS_FRAGMENT
import android.example.homescout.utils.Constants.ACTION_START_BLUETOOTH_SERVICE
import android.example.homescout.utils.Constants.ACTION_STOP_BLUETOOTH_SERVICE
import android.example.homescout.utils.Constants.CHANNEL_ID_BLUETOOTH_SCANNING
import android.example.homescout.utils.Constants.INTERVAL_BLE_SCAN
import android.example.homescout.utils.Constants.NOTIFICATION_CHANNEL_BLUETOOTH
import android.example.homescout.utils.Constants.SCAN_PERIOD
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber


class BluetoothScanningService : LifecycleService() {


    private val handler = Handler(Looper.getMainLooper())

    private val scanResults = mutableListOf<ScanResult>()
    private var isServiceRunning = false
    private var isScanning = false

    private val isBluetoothEnabled : Boolean
        get() = bluetoothAdapter.isEnabled

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = applicationContext.getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    override fun onCreate() {
        super.onCreate()
        Timber.i("Service Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (!isBluetoothEnabled) {

            MaterialAlertDialogBuilder(applicationContext)
                .setTitle("Bluetooth required!")
                .setMessage("Please enable Bluetooth. Thanks")
                .setPositiveButton("Ok") { _, _ ->
                    // Respond to positive button press

                }
                .show()

            stopSelf()
            return super.onStartCommand(intent, flags, startId)
        }

        intent?.let {

            when (it.action) {

                ACTION_START_BLUETOOTH_SERVICE -> {
                    if (!isServiceRunning) {
                        Timber.i("Start Service")
                        isServiceRunning = true
                        startForegroundService()

                    }
                }

                ACTION_STOP_BLUETOOTH_SERVICE -> {
                    Timber.i("Stop Service")
                    isServiceRunning = false
                    stopSelf()
                }

            }

        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {

        startBleScan()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        createNotificationChannel(notificationManager)

        val notificationBuilder = NotificationCompat.Builder(this,
            CHANNEL_ID_BLUETOOTH_SCANNING
        )
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_bluetooth_searching_24px)
            .setContentTitle("Home Scout")
            .setContentText("Bluetooth service is running.")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(Constants.NOTIFICATION_ID_BLUETOOTH, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_SETTINGS_FRAGMENT
        },
        PendingIntent.FLAG_IMMUTABLE
    )

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID_BLUETOOTH_SCANNING,
            NOTIFICATION_CHANNEL_BLUETOOTH,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun startBleScan() {

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.i("Permissions not granted.")
            return
        }
        Timber.i("startBleScan")
        Timber.i("isScanning: $isScanning")
        Timber.i("isServiceRunning: $isServiceRunning")

        if (!isScanning && isServiceRunning) {
            handler.postDelayed({
                isScanning = false
                bleScanner.stopScan(scanCallback)
                Timber.i("scanResults: $scanResults")
                Timber.i("scanResultsSize: ${scanResults.size}")
                Toast.makeText(
                    applicationContext,
                    "scanResultsSize: ${scanResults.size}",
                    Toast.LENGTH_SHORT
                ).show()
                handler.postDelayed({
                    startBleScan()
                }, INTERVAL_BLE_SCAN )
            }, SCAN_PERIOD)
            isScanning = true
            bleScanner.startScan(scanCallback)
        } else {
            Timber.i("stop handler")
            isScanning = false
            bleScanner.stopScan(scanCallback)
        }

    }

    private val scanCallback = object : ScanCallback() {


        override fun onScanResult(callbackType: Int, result: ScanResult) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                BluetoothAPILogger().logResults(result)
            }

            // this might needs to be changed as the device.address might change due to
            // MAC randomization
            // check if the current found result is already in the entire scanResult list
            val indexQuery = scanResults.indexOfFirst { it.device.address == result.device.address }
            // element not found returns -1
            if (indexQuery != -1) { // A scan result already exists with the same address
                scanResults[indexQuery] = result
            } else { // found new device
                scanResults.add(result)
            }
        }
    }
}