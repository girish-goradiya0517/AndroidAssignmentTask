MoviesApp

MoviesApp is an Android application built using modern development practices and architectural patterns. It features a clean, modular structure and demonstrates efficient handling of APIs, offline storage, and state management. The app uses Jetpack Compose for UI, Hilt for dependency injection, Retrofit for API integration, Room for database management, and WorkManager for background tasks.


Features

Popular and Trending Movies: Displays paginated lists of popular and trending movies.

Movie Details: Navigate to a detailed screen for each movie, showcasing key information.

Offline Support: Utilizes Room and WorkManager to provide functionality even without an internet connection.

Clean Architecture: Implements modular layers (data, domain, and presentation).

Responsive UI: Designed with Jetpack Compose to ensure seamless user experience across devices.

Architecture Overview

The app follows a Clean Architecture approach with MVVM principles:

Data Layer: Handles local and remote data sources.

Domain Layer: Encapsulates business logic and interacts with the repository layer.

Presentation Layer: Manages UI and ViewModels for state handling.

Project Structure

The following structure showcases the modular organization of the project:

com
└── example
    └── moviesapp
        ├── datasource
        │   ├── data
        │   │   ├── local
        │   │   ├── mappers
        │   │   ├── remote
        │   │   └── repository
        │   ├── domain
        │   │   ├── model
        │   │   └── repository
        ├── di
        │   ├── AppModule.kt
        │   └── RepositoryModule.kt
        ├── ui
        │   ├── core
        │   │   ├── activity
        │   │   ├── components
        │   │   ├── events
        │   │   ├── presentation
        │   │   ├── route
        │   │   ├── state
        │   │   ├── viewModel
        │   │   └── workers
        │   └── theme
        │       ├── Color.kt
        │       ├── Theme.kt
        │       └── Type.kt
        └── util
            └── App.kt

Tech Stack

Core Libraries

Kotlin: Primary programming language.

Jetpack Compose: Modern declarative UI framework.

Hilt: Dependency injection framework.

Retrofit: REST API integration.

Room: Offline database management.

WorkManager: Background task scheduling.

Key Implementations

1. Dependency Injection

Hilt is used to inject dependencies throughout the app. The AppModule and RepositoryModule classes configure all required bindings.

2. State Management

LazyPagingItems: Used for managing paginated movie lists.

State Handling: Integrated with LoadState for dynamic UI updates based on network responses.

3. Offline Support

Room Database: Caches data locally.

WorkManager: Syncs data in the background when a network is available.

4. Custom Components

LoadingBar: A reusable loading indicator composable.

Error Handling: Error-specific UI responses for better user feedback.

Screens

1. Movie List Screen

Displays trending and popular movies.

Integrates smooth scrolling with LazyVerticalGrid.

2. Movie Detail Screen

Provides details for selected movies.

API Integration

The app fetches movie data from a REST API using Retrofit. Responses are mapped into domain models and displayed via Jetpack Compose.
