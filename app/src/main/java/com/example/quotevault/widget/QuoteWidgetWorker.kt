package com.example.quotevault.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quotevault.domain.repository.QuoteRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class QuoteWidgetWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val quoteRepository: QuoteRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val quoteOfDay = quoteRepository.getQuoteOfDay()

            if (quoteOfDay != null) {
                val manager = GlanceAppWidgetManager(context)
                val glanceIds = manager.getGlanceIds(QuoteOfDayWidget::class.java)

                glanceIds.forEach { glanceId ->
                    updateWidget(
                        context = context,
                        glanceId = glanceId,
                        text = quoteOfDay.text,
                        author = quoteOfDay.author,
                        id = quoteOfDay.id
                    )
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
