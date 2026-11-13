# Weather Preferences App

## Overview
A mobile application built with Kotlin, Jetpack Compose, and Supabase as a Backend-as-a-Service (BaaS). Following Clean Architecture principles, this app manages and persists user weather preferences (favorite city and preferred temperature unit) using asynchronous CRUD operations.

[Software Demo Video](https://youtu.be/aW2cNdAi9vA) 

giturl: https://github.com/gguzmanhnd/weather

Development Environment & Tech Stack
* IDE: Android Studio
* Language: Kotlin
* UI Framework: Jetpack Compose & Material3
* Architecture: Clean Architecture (Data, Presentation, and UI Layers)
* Backend / Database: Supabase (Postgrest Kotlin SDK)
* Concurrency & State Management: Kotlin Coroutines & `StateFlow`

Key Features
* Real-Time Cloud Synchronization: Executes asynchronous `select` and `upsert` operations against a Supabase PostgreSQL table (`user_preferences`).
* Reactive UI: Dynamically updates input fields and loading states using `StateFlow` and Compose's `collectAsState()`.
* Separation of Concerns: Decouples database logic (`WeatherRepository`) from UI state management (`WeatherViewModel`).

Setup & Execution
1. Clone this repository to your local machine.
2. Open the project in Android Studio.
3. Navigate to `SupabaseClient.kt` and insert your own Supabase Project URL and Anon Key.
4. Ensure AndroidX compatibility is enabled in the root `gradle.properties` file:
   ```properties
   android.useAndroidX=true
   android.enableJetifier=true