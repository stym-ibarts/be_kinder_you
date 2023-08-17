package com.be_v2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.opencsv.CSVReader
import java.io.InputStreamReader
import java.util.Stack
import kotlin.random.Random

class NotificationReceiver() : BroadcastReceiver() {

    companion object {
        const val EXTRA_NOTIFICATION_TEXT = "extra_notification_text"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        // Create a notification channel (required for Android Oreo and above)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationHelper.CHANNEL_ID,
                "Daily Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Get the notification text from the intent
        val notificationText = intent?.getStringExtra(EXTRA_NOTIFICATION_TEXT) ?: ""
        val notificationTextFilePath = intent?.getStringExtra("filepath") ?: ""
        Log.i("text",notificationTextFilePath)

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(notificationText)


        // Create an intent to launch the main activity when the notification is clicked
        val mainActivityIntent = Intent(context, QuotesModel::class.java) // QuotesModel or NotificationQuote
        mainActivityIntent.putExtra("quote", notificationText)
        mainActivityIntent.putExtra("CSV", notificationTextFilePath)
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)


        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            mainActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        // Build the notification
        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setContentTitle("be.")
            .setContentText(notificationText)
            .setSmallIcon(R.drawable.index)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setStyle(bigTextStyle)
            .build()

        // Show the notification

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(NotificationHelper.NOTIFICATION_ID, notification)

    }


}