package com.example.quotevault.notification

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.quotevault.domain.repository.QuoteRepository
import com.example.quotevault.domain.repository.SettingsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.concurrent.TimeUnit

@HiltWorker
class DailyQuoteWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val quoteRepository: QuoteRepository,
    private val settingsRepository: SettingsRepository,
    private val notificationManager: DailyQuoteNotificationManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val settings = settingsRepository.settings.first()

            if (!settings.notificationEnabled) {
                return Result.success()
            }

            val quoteOfDay = quoteRepository.getQuoteOfDay()

            if (quoteOfDay != null) {
                notificationManager.showDailyQuoteNotification(quoteOfDay)
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "daily_quote_notification"

        fun schedule(context: Context, notificationTime: LocalTime) {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val currentTime = now.time

            var delayMinutes = (notificationTime.hour * 60 + notificationTime.minute) -
                    (currentTime.hour * 60 + currentTime.minute)

            if (delayMinutes < 0) {
                delayMinutes += 24 * 60
            }

            val workRequest = PeriodicWorkRequestBuilder<DailyQuoteWorker>(
                24, TimeUnit.HOURS
            )
                .setInitialDelay(delayMinutes.toLong(), TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
