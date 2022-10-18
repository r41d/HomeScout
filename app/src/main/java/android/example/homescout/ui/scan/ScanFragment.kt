package android.example.homescout.ui.scan

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.example.homescout.BluetoothAPILogger
import android.example.homescout.ScanResultAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.example.homescout.databinding.FragmentScanBinding
import android.example.homescout.models.DeviceFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlin.experimental.and


private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
private const val LOCATION_PERMISSION_REQUEST_CODE = 2

class ScanFragment : Fragment() {


    // PROPERTIES

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    val isLocationPermissionGranted
        get() = activity?.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private val scanResults = mutableListOf<ScanResult>()
    private var isScanning = false
        set(value) {
            field = value
            if (value) {
                binding.buttonScan.text = "Stop"
            } else {
                binding.buttonScan.text = "scan"
            }
        }


    // PROPERTIES lateinit
    private lateinit var scanViewModel: ScanViewModel
    private lateinit var scanSettings: ScanSettings



    // PROPERTIES lazy
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = activity?.getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanResultAdapter: ScanResultAdapter by lazy {
        ScanResultAdapter(scanResults) { result ->
            // User tapped on a scan result
            with(result.device) {
                Snackbar.make(binding.root, "Tapped on: $address", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    // LIFECYCLE FUNCTIONS
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupViewModelAndBinding(inflater, container)
        buildScanSettings()
        setOnClickListenerForScanButton()
        setupRecyclerView()

        requestBluetoothIsEnabled()
        requestLocationPermissionIsEnabled()

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        if (!bluetoothAdapter.isEnabled) {
            requestBluetoothIsEnabled()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    // FUNCTIONS USED IN onCreateView() (for code readability)
    private fun setupViewModelAndBinding(inflater: LayoutInflater, container: ViewGroup?) {
        scanViewModel = ViewModelProvider(this).get(ScanViewModel::class.java)
        _binding = FragmentScanBinding.inflate(inflater, container, false)
    }


    private fun buildScanSettings() {
        scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
    }

    private fun setOnClickListenerForScanButton() {
        binding.buttonScan.setOnClickListener {
            if (isScanning) {
                stopBleScan()
            } else {
                startBLEScan()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.scanResultsRecyclerView.apply {
            adapter = scanResultAdapter
            layoutManager = LinearLayoutManager(
                activity,
                RecyclerView.VERTICAL,
                false
            )
            isNestedScrollingEnabled = false
        }

        val animator = binding.scanResultsRecyclerView.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestBluetoothIsEnabled() {
        if (!bluetoothAdapter.isEnabled) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Bluetooth required")
                .setMessage("For this app to work you need to enable bluetooth")
                .setPositiveButton("Enable") { dialog, which ->
                    // Respond to positive button press
                    bluetoothAdapter.enable()
                }
                .show()
        }
    }

    // CALLBACKS
    private val scanCallback = object : ScanCallback() {
        @RequiresApi(Build.VERSION_CODES.R)
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {

            BluetoothAPILogger().logResults(result)


            // this might needs to be changed as the device.address might change due to
            // MAC randomization
            // check if the current found result is already in the entire scanResult list
            val indexQuery = scanResults.indexOfFirst { it.device.address == result.device.address }
            // element not found returns -1
            if (indexQuery != -1) { // A scan result already exists with the same address
                scanResults[indexQuery] = result
                scanResultAdapter.notifyItemChanged(indexQuery)
            } else { // found new device
//                with(result.device) {
//                    //Log.i("ScanCallback", "Found BLE device! Name: ${name ?: "Unnamed"}, address: $address")
//                }
                scanResults.add(result)
                scanResultAdapter.notifyItemInserted(scanResults.size - 1)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("ScanCallback", "onScanFailed: code $errorCode")
        }
    }


    private fun requestLocationPermissionIsEnabled() {
        if (!isLocationPermissionGranted!!) {
            MaterialAlertDialogBuilder(requireContext())
            .setTitle("Location required")
            .setMessage("For this app to work you need to enable Locations")
            .setPositiveButton("Ok") { dialog, which ->
                requestPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
            .show()
        }
    }

    // PRIVATE FUNCTIONS

    private fun Context.hasPermission(permissionType: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permissionType) ==
                PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun startBLEScan() {

        if (!bluetoothAdapter.isEnabled) { requestBluetoothIsEnabled() }

        scanResults.clear()
        scanResultAdapter.notifyDataSetChanged()
        bleScanner.startScan(null, scanSettings, scanCallback)
        isScanning = true
    }

    @SuppressLint("MissingPermission")
    private fun stopBleScan() {
        bleScanner.stopScan(scanCallback)
        isScanning = false
    }

    private fun Fragment.requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
    }


    // Scan Filter Example (not intended to use so far)

//    private val AirTagScanFilter = ScanFilter.Builder()
//        .setManufacturerData(
//            0x4C,
//            byteArrayOf((0x12).toByte(), (0x19).toByte(), (0x10).toByte())
//        )
//        .build()


}