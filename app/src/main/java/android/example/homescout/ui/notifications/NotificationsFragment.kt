package android.example.homescout.ui.notifications

import android.content.Intent
import android.example.homescout.R
import android.example.homescout.databinding.FragmentNotificationsBinding
import android.example.homescout.ui.main.MainActivity
import android.example.homescout.utils.Constants.CHANNEL_ID_DEVICE_FOUND
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.material.snackbar.Snackbar


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        binding.buttonNotify.setOnClickListener {
            Snackbar.make(binding.root, "Not implemented for now.", Snackbar.LENGTH_LONG).show()
            // createAndSendNotification()
        }






        return binding.root
    }

    private fun createAndSendNotification() {

        Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = NavDeepLinkBuilder(requireContext())
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.navigation_notifications)
            .createPendingIntent()


        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID_DEVICE_FOUND)
            .setSmallIcon(R.drawable.ic_notifications_24px)
            .setContentTitle("Hello")
            .setContentText("This is my first notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(1, builder.build())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}