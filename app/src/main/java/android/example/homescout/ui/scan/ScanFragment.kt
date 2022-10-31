package android.example.homescout.ui.scan

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Intent
import android.content.pm.PackageManager
import android.example.homescout.BluetoothAPILogger
import android.example.homescout.PermissionAppIntro
import android.example.homescout.ScanResultAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.example.homescout.databinding.FragmentScanBinding
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
private const val LOCATION_PERMISSION_REQUEST_CODE = 2

class ScanFragment : Fragment() {


    // PROPERTIES

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private val isBluetoothEnabled : Boolean
        get() = bluetoothAdapter.isEnabled
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

        checkIfBluetoothIsEnabled()

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        checkIfBluetoothIsEnabled()
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

    private fun checkIfBluetoothIsEnabled() {

        binding.buttonScan.isEnabled = isBluetoothEnabled

        if (!isBluetoothEnabled) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Bluetooth required!")
                .setMessage("Please enable Bluetooth. Thanks")
                .setPositiveButton("Ok") { dialog, which ->
                    // Respond to positive button press

                }
                .show()
        }
    }

    // CALLBACKS
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

    // PRIVATE FUNCTIONS
    private fun startBLEScan() {

        if (!isBluetoothEnabled) { checkIfBluetoothIsEnabled() }

        scanResults.clear()
        scanResultAdapter.notifyDataSetChanged()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            startActivity(Intent(requireContext(), PermissionAppIntro::class.java))
            return
        }
        bleScanner.startScan(null, scanSettings, scanCallback)
        isScanning = true
    }

    private fun stopBleScan() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            startActivity(Intent(requireContext(), PermissionAppIntro::class.java))
            return
        }
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