package android.example.homescout.ui.settings

import android.content.Intent
import android.example.homescout.R
import android.example.homescout.databinding.FragmentSettingsBinding
import android.example.homescout.services.TrackingService
import android.example.homescout.utils.Constants.ACTION_START_SERVICE
import android.example.homescout.utils.Constants.ACTION_STOP_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment() {


    // PROPERTIES
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingsViewModel: SettingsViewModel

    private val touchListenerDistance: Slider.OnSliderTouchListener =
        object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Not needed
            }

            override fun onStopTrackingTouch(slider: Slider) {
                settingsViewModel.updateDistance(slider.value)
            }
        }

    private val touchListenerTime: Slider.OnSliderTouchListener =
        object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Not needed
            }

            override fun onStopTrackingTouch(slider: Slider) {
                settingsViewModel.updateTime(slider.value)
            }
        }

    private val touchListenerOccurrences: Slider.OnSliderTouchListener =
        object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Not needed
            }

            override fun onStopTrackingTouch(slider: Slider) {
                settingsViewModel.updateOccurrences(slider.value)
            }
        }


    // LIFECYCLE FUNCTIONS
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupViewModelAndBinding(inflater, container)
        setupSwitchTrackingProtection()
        setupSliderDistance()
        setupSliderTime()
        setupSliderOccurences()
        setupColorChangeForInfoButtons()
        observeTrackingPreferences()
        addOnSliderTouchListeners()

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // FUNCTIONS USED IN LIFECYCLE (for code readability)
    private fun setupViewModelAndBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.settingsViewModel = settingsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupSliderDistance() {
        binding.sliderDistance.setLabelFormatter { value: Float ->
            "%.0f m".format(value)
        }

        binding.infoSliderDistance.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.description_slider_distance))
                .setMessage(getString(R.string.text_dialog_slider_distance))
                .setPositiveButton("Ok") { dialog, which ->
                    // No Respond to positive button press needed
                }
                .show()
        }
    }

    private fun setupSwitchTrackingProtection() {
        binding.switchTrackingProtection.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.onSwitchToggled(checked)
            if (checked) {
                sendCommandToService(ACTION_START_SERVICE)
            } else {
                sendCommandToService(ACTION_STOP_SERVICE)
            }
        }
    }

    private fun setupSliderTime() {
        binding.sliderTime.setLabelFormatter { value: Float ->
            "%.0f min".format(value)
        }

        binding.infoSliderTime.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.description_slider_time))
                .setMessage(getString(R.string.text_dialog_slider_time))
                .setPositiveButton("Ok") { dialog, which ->
                    // No Respond to positive button press needed
                }
                .show()
        }
    }

    private fun setupSliderOccurences() {
        binding.sliderOccurrences.setLabelFormatter { value: Float ->
            if (value == 1.0f) "%.0f time".format(value) else "%.0f times".format(value)
        }



        binding.infoSliderOccurrences.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.description_slider_occurrences))
                .setMessage(getString(R.string.text_dialog_slider_occurrences))
                .setPositiveButton("Ok") { dialog, which ->
                    // No Respond to positive button press needed
                }
                .show()
        }
    }

    private fun setupColorChangeForInfoButtons() {
        settingsViewModel.isSwitchEnabled.observe(viewLifecycleOwner) {

            if (it) {
                binding.infoSliderDistance.setColorFilter(
                    getColor(
                        requireContext(),
                        R.color.purple_500
                    )
                )
                binding.infoSliderTime.setColorFilter(
                    getColor(
                        requireContext(),
                        R.color.purple_500
                    )
                )
                binding.infoSliderOccurrences.setColorFilter(
                    getColor(
                        requireContext(),
                        R.color.purple_500
                    )
                )
            } else {
                binding.infoSliderDistance.setColorFilter(getColor(requireContext(), R.color.grey))
                binding.infoSliderTime.setColorFilter(getColor(requireContext(), R.color.grey))
                binding.infoSliderOccurrences.setColorFilter(
                    getColor(
                        requireContext(),
                        R.color.grey
                    )
                )
            }
        }
    }


    private fun observeTrackingPreferences() {
        settingsViewModel.distance.observe(viewLifecycleOwner) {
            binding.sliderDistance.value = it
        }

        settingsViewModel.time.observe(viewLifecycleOwner) {
            binding.sliderTime.value = it
        }

        settingsViewModel.occurrences.observe(viewLifecycleOwner) {
            binding.sliderOccurrences.value = it
        }
    }

    private fun addOnSliderTouchListeners() {
        binding.sliderDistance.addOnSliderTouchListener(touchListenerDistance)
        binding.sliderTime.addOnSliderTouchListener(touchListenerTime)
        binding.sliderOccurrences.addOnSliderTouchListener(touchListenerOccurrences)
    }

    // PRIVATE FUNCTIONS
    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            // does not actually start service, but delivers the intent to the service
            requireContext().startService(it)
        }
}