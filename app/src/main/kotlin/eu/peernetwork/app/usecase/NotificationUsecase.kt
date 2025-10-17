package eu.peernetwork.app.usecase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import eu.peernetwork.app.R
import eu.peernetwork.core.common.usecase.ParameterizedBlockingUseCase
import javax.inject.Inject

class NotificationUsecase @Inject constructor(
    private val context: Context
) : ParameterizedBlockingUseCase<RemoteMessage, Unit> {
    private val channel = this::class.java.name

    private val title: String by lazy {
        context.getString(R.string.app_name)
    }

    private val caption: String by lazy {
        context.getString(R.string.notification_caption)
    }

    override fun invoke(param: RemoteMessage) {
        val title = param.notification?.title ?: title
        val message = param.notification?.body ?: caption
        val notificationBuilder = NotificationCompat.Builder(context, channel)
            .setSmallIcon(R.drawable.ic_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channel, title, NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}
