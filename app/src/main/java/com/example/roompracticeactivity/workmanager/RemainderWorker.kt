package com.example.roompracticeactivity.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.activity.HomeActivity
import com.example.roompracticeactivity.database.NotesRoomDatabase
import com.example.roompracticeactivity.database.dao.NotesDao
import com.example.roompracticeactivity.fragment.AddNoteFragment.Companion.UNIQUE_ID
import com.example.roompracticeactivity.fragment.EditNotesFragment.Companion.NOTES

class RemainderWorker(private val ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val notesDao: NotesDao = NotesRoomDatabase.getDatabase(ctx).notesDao()
        val notes = notesDao.getNotes(inputData.getLong(UNIQUE_ID, 0).toString())
        val bundle = Bundle().apply {
            putSerializable(NOTES, notes)
        }

        val pendingIntent = NavDeepLinkBuilder(ctx)
            .setComponentName(HomeActivity::class.java) // your destination activity
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.editNotesFragment)
            .setArguments(bundle)
            .createPendingIntent()
        val channelId = "channelId"
        val channelName = "channelName"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(ctx, channelId)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle(notes.notesTitle)
            .setContentText("Tap to view notes")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())

        return Result.success()
    }

}