package com.example.quotevault.core.common

object Constants {
    // Supabase Configuration
    // TODO: Replace with your Supabase project URL and anon key
    const val SUPABASE_URL = "YOUR_SUPABASE_URL"
    const val SUPABASE_ANON_KEY = "YOUR_SUPABASE_ANON_KEY"

    // Table Names
    object Tables {
        const val QUOTES = "quotes"
        const val CATEGORIES = "categories"
        const val QUOTE_OF_DAY = "quote_of_day"
        const val USER_PROFILES = "user_profiles"
        const val USER_FAVORITES = "user_favorites"
        const val COLLECTIONS = "collections"
        const val COLLECTION_QUOTES = "collection_quotes"
        const val USER_SETTINGS = "user_settings"
    }

    // Category IDs (will be populated from database)
    object Categories {
        const val MOTIVATION = "motivation"
        const val LOVE = "love"
        const val SUCCESS = "success"
        const val WISDOM = "wisdom"
        const val HUMOR = "humor"
    }

    // Preferences Keys
    object Prefs {
        const val THEME_MODE = "theme_mode"
        const val ACCENT_COLOR = "accent_color"
        const val FONT_SIZE = "font_size"
        const val NOTIFICATION_ENABLED = "notification_enabled"
        const val NOTIFICATION_TIME = "notification_time"
    }

    // Pagination
    const val PAGE_SIZE = 20

    // Notification
    const val NOTIFICATION_CHANNEL_ID = "daily_quote_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Daily Quote"
    const val DAILY_QUOTE_NOTIFICATION_ID = 1001

    // Intent Extras
    const val EXTRA_QUOTE_ID = "quote_id"

    // Work Manager Tags
    object WorkTags {
        const val DAILY_QUOTE_NOTIFICATION = "daily_quote_notification"
        const val WIDGET_UPDATE = "widget_update"
        const val SYNC_FAVORITES = "sync_favorites"
    }

    // Deep Links
    object DeepLinks {
        const val QUOTE_DETAIL = "quotevault://quote/"
        const val COLLECTION_DETAIL = "quotevault://collection/"
    }

    // Share Constants
    object Share {
        const val APP_NAME = "QuoteVault"
        const val SHARE_TEXT_TEMPLATE = "\"%s\"\n\n- %s\n\nShared via QuoteVault"
    }
}
