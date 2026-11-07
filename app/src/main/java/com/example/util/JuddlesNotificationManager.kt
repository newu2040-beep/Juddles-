package com.example.util

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

data class CustomDateReminder(
  val id: String,
  val title: String,
  val category: String = "Bill Reminder", // Bill Reminder, Goal Milestone, Budget Check-in, Subscription, Tax & EMI
  val targetDateText: String, // e.g. "2026-11-15"
  val targetTimeText: String = "09:00 AM",
  val amountMinor: Long = 0L,
  val notifyPush: Boolean = true,
  val isCompleted: Boolean = false
)

class JuddlesReminderReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    val title = intent.getStringExtra("reminder_title") ?: "Juddles Financial Reminder"
    val body = intent.getStringExtra("reminder_body") ?: "You have a scheduled financial check-in today."
    JuddlesNotificationManager.sendInstantNotification(
      context = context,
      title = title,
      message = body,
      vibrate = true
    )
  }
}

object JuddlesNotificationManager {
  private const val CHANNEL_ID = "juddles_reminders_v3"
  private const val PREFS_NAME = "juddles_v3_preferences"

  val reminderCategories = listOf(
    "Bill Reminder",
    "Goal Milestone",
    "Budget Check-in",
    "Subscription Renewal",
    "Tax & EMI",
    "Custom Alert"
  )

  fun hasNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
      ) == PackageManager.PERMISSION_GRANTED
    } else {
      true
    }
  }

  fun ensureNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
      val existing = manager.getNotificationChannel(CHANNEL_ID)
      if (existing == null) {
        val channel = NotificationChannel(
          CHANNEL_ID,
          "Juddles Financial Reminders & Alerts",
          NotificationManager.IMPORTANCE_HIGH
        ).apply {
          description = "Custom date reminders, bill due alerts, and budget threshold notifications"
          enableVibration(true)
        }
        manager.createNotificationChannel(channel)
      }
    }
  }

  fun triggerHapticFeedback(context: Context) {
    runCatching {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vm?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(45L, VibrationEffect.DEFAULT_AMPLITUDE))
      } else {
        @Suppress("DEPRECATION")
        val vib = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          vib?.vibrate(VibrationEffect.createOneShot(45L, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
          @Suppress("DEPRECATION")
          vib?.vibrate(45L)
        }
      }
    }
  }

  fun sendInstantNotification(
    context: Context,
    title: String,
    message: String,
    vibrate: Boolean = true
  ): Boolean {
    if (vibrate) {
      triggerHapticFeedback(context)
    }
    if (!hasNotificationPermission(context)) {
      return false
    }
    return runCatching {
      ensureNotificationChannel(context)
      val openIntent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
      }
      val pendingIntent = PendingIntent.getActivity(
        context,
        title.hashCode(),
        openIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
      )

      val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(title)
        .setContentText(message)
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()

      NotificationManagerCompat.from(context).notify(
        (System.currentTimeMillis() % 100000).toInt(),
        notification
      )
      true
    }.getOrDefault(false)
  }

  fun scheduleReminderAlarm(
    context: Context,
    reminder: CustomDateReminder,
    formattedAmount: String
  ) {
    runCatching {
      val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
      val parser = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US)
      val parsedDate = runCatching {
        parser.parse("${reminder.targetDateText} ${reminder.targetTimeText}")
      }.getOrNull()

      val triggerAtMillis = parsedDate?.time?.takeIf { it > System.currentTimeMillis() }
        ?: (System.currentTimeMillis() + 60_000L)

      val intent = Intent(context, JuddlesReminderReceiver::class.java).apply {
        putExtra("reminder_title", "Juddles • ${reminder.category}: ${reminder.title}")
        val amountSuffix = if (reminder.amountMinor > 0L) " ($formattedAmount)" else ""
        putExtra(
          "reminder_body",
          "Scheduled for ${reminder.targetDateText} at ${reminder.targetTimeText}$amountSuffix"
        )
      }
      val pendingIntent = PendingIntent.getBroadcast(
        context,
        reminder.id.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
      )
      alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }
  }

  fun loadFontPreference(context: Context, userId: String): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getString("font_mode_$userId", "PLUS_JAKARTA") ?: "PLUS_JAKARTA"
  }

  fun saveFontPreference(context: Context, userId: String, fontKey: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putString("font_mode_$userId", fontKey).apply()
  }

  fun loadHapticsEnabled(context: Context, userId: String): Boolean {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getBoolean("haptics_$userId", true)
  }

  fun saveHapticsEnabled(context: Context, userId: String, enabled: Boolean) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putBoolean("haptics_$userId", enabled).apply()
  }

  fun loadSavedExportFolderUri(context: Context, userId: String): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getString("export_folder_uri_$userId", "") ?: ""
  }

  fun saveExportFolderUri(context: Context, userId: String, uriString: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putString("export_folder_uri_$userId", uriString).apply()
  }

  fun loadReminders(context: Context, userId: String): List<CustomDateReminder> {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val raw = prefs.getString("reminders_$userId", null)
    if (raw.isNullOrBlank()) {
      return defaultStarterReminders()
    }
    return runCatching {
      val arr = JSONArray(raw)
      buildList {
        for (i in 0 until arr.length()) {
          val obj = arr.getJSONObject(i)
          add(
            CustomDateReminder(
              id = obj.optString("id", "rem_$i"),
              title = obj.optString("title", "Reminder"),
              category = obj.optString("category", "Bill Reminder"),
              targetDateText = obj.optString("targetDateText", "2026-11-15"),
              targetTimeText = obj.optString("targetTimeText", "09:00 AM"),
              amountMinor = obj.optLong("amountMinor", 0L),
              notifyPush = obj.optBoolean("notifyPush", true),
              isCompleted = obj.optBoolean("isCompleted", false)
            )
          )
        }
      }
    }.getOrElse { defaultStarterReminders() }
  }

  fun saveReminders(context: Context, userId: String, reminders: List<CustomDateReminder>) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val arr = JSONArray()
    reminders.forEach { r ->
      val obj = JSONObject().apply {
        put("id", r.id)
        put("title", r.title)
        put("category", r.category)
        put("targetDateText", r.targetDateText)
        put("targetTimeText", r.targetTimeText)
        put("amountMinor", r.amountMinor)
        put("notifyPush", r.notifyPush)
        put("isCompleted", r.isCompleted)
      }
      arr.put(obj)
    }
    prefs.edit().putString("reminders_$userId", arr.toString()).apply()
  }

  private fun defaultStarterReminders(): List<CustomDateReminder> = listOf(
    CustomDateReminder(
      id = "rem_starter_1",
      title = "Monthly Utilities & Internet Bill",
      category = "Bill Reminder",
      targetDateText = "2026-11-15",
      targetTimeText = "09:00 AM",
      amountMinor = 28000L,
      notifyPush = true,
      isCompleted = false
    ),
    CustomDateReminder(
      id = "rem_starter_2",
      title = "Transfer to Buy a House Goal",
      category = "Goal Milestone",
      targetDateText = "2026-11-25",
      targetTimeText = "06:00 PM",
      amountMinor = 50000L,
      notifyPush = true,
      isCompleted = false
    )
  )
}
