package com.example.quotevault.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.quotevault.MainActivity
import com.example.quotevault.R
import com.example.quotevault.core.common.Constants

class QuoteOfDayWidget : GlanceAppWidget() {

    companion object {
        val quoteTextKey = stringPreferencesKey("quote_text")
        val quoteAuthorKey = stringPreferencesKey("quote_author")
        val quoteIdKey = stringPreferencesKey("quote_id")
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                QuoteWidgetContent()
            }
        }
    }

    @Composable
    private fun QuoteWidgetContent() {
        val prefs = currentState<Preferences>()
        val quoteText = prefs[quoteTextKey] ?: "Tap to load today's quote"
        val quoteAuthor = prefs[quoteAuthorKey] ?: ""
        val quoteId = prefs[quoteIdKey] ?: ""

        val quoteIdParam = ActionParameters.Key<String>("quote_id")

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.primaryContainer)
                .clickable(actionRunCallback<OpenAppAction>(
                    actionParametersOf(quoteIdParam to quoteId)
                ))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Quote icon
                Image(
                    provider = ImageProvider(R.drawable.ic_quote),
                    contentDescription = "Quote",
                    modifier = GlanceModifier.size(24.dp)
                )

                Spacer(modifier = GlanceModifier.height(8.dp))

                // Quote text
                Text(
                    text = "\"$quoteText\"",
                    style = TextStyle(
                        color = GlanceTheme.colors.onPrimaryContainer,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic
                    ),
                    maxLines = 4
                )

                if (quoteAuthor.isNotEmpty()) {
                    Spacer(modifier = GlanceModifier.height(8.dp))

                    // Author
                    Text(
                        text = "— $quoteAuthor",
                        style = TextStyle(
                            color = GlanceTheme.colors.onPrimaryContainer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1
                    )
                }

                Spacer(modifier = GlanceModifier.height(12.dp))

                // App branding
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.ic_launcher_foreground),
                        contentDescription = "QuoteVault",
                        modifier = GlanceModifier.size(16.dp)
                    )
                    Spacer(modifier = GlanceModifier.width(4.dp))
                    Text(
                        text = "QuoteVault",
                        style = TextStyle(
                            color = GlanceTheme.colors.onPrimaryContainer,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }
    }
}

class OpenAppAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val quoteId = parameters[ActionParameters.Key<String>("quote_id")]

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            if (!quoteId.isNullOrEmpty()) {
                putExtra(Constants.EXTRA_QUOTE_ID, quoteId)
            }
        }
        context.startActivity(intent)
    }
}

suspend fun updateWidget(context: Context, glanceId: GlanceId, text: String, author: String, id: String) {
    updateAppWidgetState(context, glanceId) { prefs ->
        prefs[QuoteOfDayWidget.quoteTextKey] = text
        prefs[QuoteOfDayWidget.quoteAuthorKey] = author
        prefs[QuoteOfDayWidget.quoteIdKey] = id
    }
    QuoteOfDayWidget().update(context, glanceId)
}
