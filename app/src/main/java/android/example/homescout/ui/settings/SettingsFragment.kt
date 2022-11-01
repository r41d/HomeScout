package android.example.homescout.ui.settings

import android.example.homescout.R
import android.example.homescout.databinding.FragmentSettingsBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.Float


class SettingsFragment : Fragment() {


    // PROPERTIES
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!


    // LIFECYCLE FUNCTIONS
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupSliderDistance()
        setupSliderTime()
        setupSliderOccurences()

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // FUNCTIONS USED IN onCreateView() (for code readability)
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
}