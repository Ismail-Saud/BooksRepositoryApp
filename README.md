# BooksRepositoryApp

A modern Kotlin Multiplatform (KMP) application for browsing books, managing a shopping cart, managing user profiles, and supporting offline-first access and cloud sync.

## 🚀 Evolution of the Project
Initially, this project was built as a local-only Android application using **Room** for data storage and **SharedPreferences** for session management. To support cross-device synchronization, secure authentication, and multiplatform support, the project was migrated to **Kotlin Multiplatform (KMP)** and **Firebase**.

- **Kotlin Multiplatform**: Shared core logic, UI, networking, navigation, and state management across Android and iOS/Native targets.
- **Authentication**: Migrated from custom Room-based logic to **Firebase Auth**.
- **Data Storage**: Migrated cloud persistence from local Room to **Cloud Firestore**.
- **Remote Configuration**: Integrated **Firebase Remote Config** for dynamic application parameters (checkout flags, shipping fees, address limits, maintenance mode).
- **Local Caching**: **Room** is utilized as a local cache for books (offline-first approach).
- **Networking**: Migrated to **Ktor Client** with **Kotlinx Serialization**.
- **Dependency Injection**: Integrated **Koin** for dependency injection across multiplatform source sets.
- **Localization**: Added multi-language support (English and Urdu) powered by Compose Multiplatform Resources and DataStore Preferences.

## 🛠 Tech Stack
- **Language**: Kotlin Multiplatform (KMP)
- **UI**: Compose Multiplatform / Jetpack Compose (Material 3)
- **Architecture**: MVVM + Clean Architecture
- **Networking**: Ktor Client & Kotlinx Serialization (OpenLibrary API)
- **Database**: Room (Local Cache) & Cloud Firestore (Cloud Sync)
- **Authentication**: Firebase Auth
- **Remote Config**: Firebase Remote Config
- **Async**: Kotlin Coroutines & Flow
- **Dependency Injection**: Koin
- **Navigation**: Navigation Compose
- **Image Loading**: Coil 3 with Ktor3 engine
- **Localization**: DataStore Preferences & Compose Multiplatform Resources

## 📦 Key Features
- **Browse Books**: Search and filter books by category and price with cached offline access.
- **Cart Management**: Add books to cart with real-time Firestore synchronization.
- **Address Management**: Manage multiple shipping addresses synced to cloud storage.
- **User Authentication**: Secure signup/login via Firebase.
- **Dynamic Config**: Real-time feature flags and configuration (shipping fees, address limit, maintenance screen).
- **Multi-Language Support**: Seamless language switching between English and Urdu.
- **Offline Support**: View previously fetched books without an active internet connection.

---
*Note: Legacy code for Room-based authentication has been removed to maintain the Single Source of Truth, while the architectural transition is documented in the project's development history.*
