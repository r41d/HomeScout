package android.example.homescout.ui.welcome

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.example.homescout.databinding.FragmentWelcomeBinding
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val LOCATION_PERMISSION_REQUEST_CODE = 2

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val isLocationPermissionGranted
        get() = activity?.hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.

            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.

            }
        }


    // LIFECYCLE FUNCTIONS
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(WelcomeViewModel::class.java)

        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        binding.switchEnableLocation.setOnClickListener {
            requestLocationPermissionIsEnabled()
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // PRIVATE FUNCTIONS

    private fun requestLocationPermissionIsEnabled() {
        if (!isLocationPermissionGranted!!) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Location Required!")
                .setMessage("For this app to work you need to let it access your location in the background. This means the app needs to access your location \"All the time.")
                .setPositiveButton("ALLOW") { dialog, which ->
                    requestPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
                .show()
        }
    }

    private fun Fragment.requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
    }

    private fun Context.hasPermission(permissionType: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permissionType) ==
                PackageManager.PERMISSION_GRANTED
    }
}