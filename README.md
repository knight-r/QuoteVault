# QuoteVault

A modern Android quote discovery and collection app built with Kotlin, Jetpack Compose, and Supabase.

## Features

### Authentication & User Accounts
- Email/password authentication with Supabase Auth
- User profile management with avatar upload
- Secure session persistence

### Quote Browsing & Discovery
- Browse 100+ curated quotes across 5 categories
- Daily Quote of the Day feature
- Search quotes by keyword or author
- Infinite scroll with pagination
- Pull-to-refresh functionality
- Offline-first architecture with Room caching

### Categories
- **Motivation** - Inspirational quotes to fuel your drive
- **Love** - Heartfelt quotes about love and relationships
- **Success** - Wisdom from successful people
- **Wisdom** - Timeless philosophical insights
- **Humor** - Light-hearted quotes to brighten your day

### Favorites & Collections
- Mark quotes as favorites with heart icon
- Create custom collections to organize quotes
- Add/remove quotes from collections
- Cloud sync across devices when logged in

### Daily Quote Notifications
- Schedule daily inspirational notifications
- Customizable notification time
- Deep link to quote detail on tap

### Share Functionality
- Share quotes as text
- Create beautiful quote card images with 4 templates:
  - Elegant (cream background)
  - Minimal (clean white)
  - Gradient (purple gradient)
  - Dark (dark theme)
- Save quote cards to gallery
- Share via any app

### Personalization
- Light/Dark/System theme modes
- 5 accent color options
- 3 font size preferences (Small, Medium, Large)
- Settings sync to cloud for logged-in users

### Home Screen Widget
- Quote of the Day widget
- Tap to open app
- Automatic daily updates

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Kotlin 2.0 |
| UI Framework | Jetpack Compose + Material3 |
| Architecture | MVVM + Clean Architecture |
| Dependency Injection | Hilt |
| Backend | Supabase (Auth + PostgreSQL) |
| Local Storage | Room + DataStore |
| Networking | Ktor (Supabase SDK) |
| Image Loading | Coil |
| Notifications | WorkManager |
| Widget | Glance (Compose-based) |
| Navigation | Navigation Compose |

## Project Structure

```
com.example.quotevault/
├── QuoteVaultApplication.kt      # @HiltAndroidApp entry point
├── MainActivity.kt               # Compose host with navigation
├── core/
│   ├── common/                   # Result, Constants, Extensions
│   ├── di/                       # Hilt modules
│   └── ui/
│       ├── theme/                # Material3 theming
│       └── components/           # Reusable UI components
├── data/
│   ├── local/
│   │   ├── database/             # Room DB, DAOs, Entities
│   │   └── datastore/            # User preferences
│   ├── remote/
│   │   ├── dto/                  # Data Transfer Objects
│   │   └── SupabaseClient.kt     # Supabase configuration
│   ├── repository/               # Repository implementations
│   └── mapper/                   # DTO <-> Entity <-> Domain
├── domain/
│   ├── model/                    # Domain models
│   └── repository/               # Repository interfaces
├── presentation/
│   ├── navigation/               # NavHost, routes, BottomNav
│   ├── auth/                     # Auth screens & ViewModels
│   ├── home/                     # Home screen
│   ├── browse/                   # Categories & browse
│   ├── search/                   # Search functionality
│   ├── favorites/                # Favorites screen
│   ├── collections/              # Collections management
│   ├── quotedetail/              # Quote detail view
│   ├── settings/                 # App settings
│   └── share/                    # Share quote sheet
├── widget/                       # Glance widget
└── notification/                 # Daily quote notifications
```

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17+
- Android SDK 36

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/QuoteVault.git
cd QuoteVault
```

### 2. Supabase Setup

1. Create a new project at [supabase.com](https://supabase.com)

2. Run the database schema:
   - Go to SQL Editor in Supabase dashboard
   - Copy and run `supabase/schema.sql`
   - Then run `supabase/seed.sql` to populate quotes

3. Create a storage bucket:
   - Go to Storage in Supabase dashboard
   - Create a new bucket named `avatars`
   - Set it to public

4. Get your credentials:
   - Go to Settings > API
   - Copy the Project URL and anon/public key

### 3. Configure the App

Update `app/src/main/java/com/example/quotevault/core/common/Constants.kt`:

```kotlin
object Constants {
    const val SUPABASE_URL = "https://your-project.supabase.co"
    const val SUPABASE_ANON_KEY = "your-anon-key"
    // ...
}
```

### 4. Build & Run

```bash
./gradlew assembleDebug
```

Or open in Android Studio and run on device/emulator.

## Testing Checklist

### Auth Flow
- [ ] Sign up with email/password
- [ ] Login with existing account
- [ ] Logout
- [ ] Password reset flow
- [ ] Profile edit with avatar

### Quote Browsing
- [ ] Home feed loads with Quote of Day
- [ ] Infinite scroll pagination
- [ ] Pull to refresh
- [ ] Categories load correctly
- [ ] Category quotes display

### Search
- [ ] Search by keyword
- [ ] Search by author
- [ ] Results display correctly

### Favorites
- [ ] Add quote to favorites
- [ ] View favorites screen
- [ ] Remove from favorites
- [ ] Cloud sync (login on another device)

### Collections
- [ ] Create new collection
- [ ] Add quotes to collection
- [ ] View collection detail
- [ ] Remove quotes from collection
- [ ] Delete collection

### Notifications
- [ ] Enable notifications
- [ ] Set notification time
- [ ] Receive daily notification
- [ ] Tap opens quote

### Sharing
- [ ] Share as text
- [ ] All 4 card templates work
- [ ] Save to gallery
- [ ] Share image

### Settings
- [ ] Theme mode toggle
- [ ] Accent color change
- [ ] Font size adjustment
- [ ] Settings persist

### Widget
- [ ] Add widget to home screen
- [ ] Displays quote
- [ ] Tap opens app
- [ ] Daily update works

### Offline Mode
- [ ] App works without network
- [ ] Shows cached data
- [ ] Syncs when back online

## Build Commands

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Clean build
./gradlew clean build
```

## License

MIT License - See LICENSE file for details.

## Credits

Built with modern Android development best practices using:
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Supabase](https://supabase.com)
- [Hilt](https://dagger.dev/hilt/)
- [Material Design 3](https://m3.material.io/)
