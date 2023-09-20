package com.ad.taskreminderapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.Random

class TaskReminderReceiver : BroadcastReceiver() {

    private var CHANNEL_ID = "task_reminder_channel"
    private var importance = NotificationManager.IMPORTANCE_HIGH
    private var mChannel: NotificationChannel? = null

    override fun onReceive(context: Context, intent: Intent?) {
        val taskTitle = intent?.getStringExtra("taskTitle") ?: "Task Reminder"
        showNotification(context, taskTitle)
    }

    private fun showNotification(context: Context, taskTitle: String) {
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name: CharSequence = context.getString(R.string.channel_name)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        }

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(taskTitle)
            .setContentText("Your task is due now!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(CHANNEL_ID)
            mBuilder.color = ContextCompat.getColor(context, R.color.app_color)
            mNotificationManager.createNotificationChannel(mChannel!!)
        } else {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher)
        }

        val random = Random()
        val notificationId: Int = random.nextInt(9999 - 1000) + 1000
        mNotificationManager.notify(notificationId, mBuilder.build())

    }
}