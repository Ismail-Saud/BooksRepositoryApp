# BooksRepositoryApp

A modern Android application for browsing books, managing a cart, and handling user profiles. 
This project demonstrates a migration from a local-only architecture to a cloud-synced architecture using Firebase.

## 🚀 Evolution of the Project
Initially, this project was built as a local-only application using **Room** for data storage and **SharedPreferences** for session management. To support cross-device synchronization and secure authentication, the project was migrated to **Firebase**.

- **Authentication**: Migrated from custom Room-based logic to **Firebase Auth**.
- **Data Storage**: Migrated from **Room** to **Cloud Firestore**.
- **Image Storage**: **Firebase Storage** (in progress) for profile pictures.
- **Local Caching**: **Room** is now utilized as a local cache for books (offline-first approach).

## 🛠 Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose (Modern, declarative UI)
- **Architecture**: MVVM + Clean Architecture
- **Networking**: Retrofit & Gson (OpenLibrary API)
- **Database**: Room (Local Cache) & Cloud Firestore (Cloud Sync)
- **Async**: Kotlin Coroutines & Flow
- **Dependency Injection**: Manual Injection (Hilt migration planned)
- **Navigation**: Navigation Compose

## 📦 Key Features
- **Browse Books**: Search and filter books by category and price.
- **Cart Management**: Add books to cart with real-time Firestore sync.
- **Address Management**: Manage multiple shipping addresses.
- **User Authentication**: Secure signup/login via Firebase.
- **Offline Support**: View previously fetched books without an internet connection.

---
*Note: Legacy code for Room-based authentication has been removed to maintain the Single Source of Truth, while the architectural transition is documented in the project's development history.*
