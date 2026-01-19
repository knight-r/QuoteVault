package com.example.quotevault.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class QuoteWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = QuoteOfDayWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        scheduleWidgetUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        cancelWidgetUpdate(context)
    }

    companion object {
        private const val WIDGET_UPDATE_WORK = "quote_widget_update"

        fun scheduleWidgetUpdate(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<QuoteWidgetWorker>(
                1, TimeUnit.HOURS
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WIDGET_UPDATE_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }

        private fun cancelWidgetUpdate(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WIDGET_UPDATE_WORK)
        }
    }
}
