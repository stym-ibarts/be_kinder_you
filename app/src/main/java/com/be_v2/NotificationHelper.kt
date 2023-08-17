package com.be_v2

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.opencsv.CSVReader
import java.io.InputStreamReader
import java.util.Calendar
import java.util.Stack

class NotificationHelper : Application() {


    companion object {
        const val CHANNEL_ID = "DailyNotificationChannel"
        const val NOTIFICATION_ID = 0
        private const val ALARM_REQUEST_CODE = 100

        fun scheduleNotification(context: Context, notificationTexts: Array<String>) {

//            val notificationTexts = notificationTexts

            fun readCsvFile(csvFilePath: String): List<String> {
                val inputStream = context.assets.open(csvFilePath)
                val reader = CSVReader(InputStreamReader(inputStream))
                val textData = mutableListOf<String>()


                var record: Array<String>?
                while (reader.readNext().also { record = it } != null) {
                    record?.let {
                        textData.add(it[0]) // Assuming the text is in the first column (index 0)
                    }
                }
                reader.close()
                return textData
            }

            fun getSelectedCSVs(): Stack<String> {
                val answer = Stack<String>()
                tickListChecker.forEachIndexed { index, element ->
                    if (element) answer.push(listCSVEnglish[index])
                }
                return answer
            }

            fun getQuoteFromSelectedCategories(): String {

                val quote: String
                val selectedCSVs = getSelectedCSVs()
                val randomSelectedCSV = selectedCSVs.random()
                val quotesFromCSV = readCsvFile(randomSelectedCSV)
                quote = quotesFromCSV.random()
                return quote

            }


            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            // Create intents to start the notification receiver
            val intent1 = Intent(context, NotificationReceiver::class.java).apply {
//                putExtra(NotificationReceiver.EXTRA_NOTIFICATION_TEXT, notificationTexts[0])
                putExtra(
                    NotificationReceiver.EXTRA_NOTIFICATION_TEXT,
                    getQuoteFromSelectedCategories()
                )
            }
            val intent2 = Intent(context, NotificationReceiver::class.java).apply {
                putExtra(NotificationReceiver.EXTRA_NOTIFICATION_TEXT, notificationTexts[1])

            }
            val intent3 = Intent(context, NotificationReceiver::class.java).apply {
                putExtra(NotificationReceiver.EXTRA_NOTIFICATION_TEXT, notificationTexts[2])

            }


            val requestCode1 = 100
            val requestCode2 = 200
            val requestCode3 = 300

            val pendingIntent1 = PendingIntent.getBroadcast(
                context,
                requestCode1,
                intent1,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val pendingIntent2 = PendingIntent.getBroadcast(
                context,
                requestCode2,
                intent2,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val pendingIntent3 = PendingIntent.getBroadcast(
                context,
                requestCode3,
                intent3,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Set the desired time for the alarms
            val calendar1 = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 7)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            val calendar2 = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 15)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            val calendar3 = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 21)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }

            // Check if the scheduled times are in the past, if so, add one day
            if (calendar1.before(Calendar.getInstance())) {
                calendar1.add(Calendar.DATE, 1)
            }
            if (calendar2.before(Calendar.getInstance())) {
                calendar2.add(Calendar.DATE, 1)
            }
            if (calendar3.before(Calendar.getInstance())) {
                calendar3.add(Calendar.DATE, 1)
            }

            // Set the alarms to repeat daily
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar1.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent1
            )
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar2.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent2
            )
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar3.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent3
            )


        }


    }


}