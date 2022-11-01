package android.example.homescout.ui.notifications

import android.app.PendingIntent
import android.content.Intent
import android.example.homescout.MainActivity
import android.example.homescout.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.example.homescout.databinding.FragmentNotificationsBinding
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

private const val CHANNEL_ID = "1"

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
            createAndSendNotification()
        }






        return binding.root
    }

    private fun createAndSendNotification() {

        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = NavDeepLinkBuilder(requireContext())
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.navigation_notifications)
            .createPendingIntent()


        var builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
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