package eu.peernetwork.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import eu.peernetwork.app.R

class MessagingService : FirebaseMessagingService() {
    private val channel = this::class.java.name

    private val title: String by lazy {
        getString(R.string.app_name)
    }

    private val caption: String by lazy {
        getString(R.string.notification_caption)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title ?: title
        val message = message.notification?.body ?: caption
        sendNotification(title, message)
    }

    private fun sendNotification(title: String, message: String) {
        val notificationBuilder = NotificationCompat.Builder(this, channel)
            .setSmallIcon(R.drawable.ic_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channel, title, NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}
