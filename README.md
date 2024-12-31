# SchoolMate (MBA USP-ESALQ)
Android app school organizer built with Android JetPack features and MVVM and Clean Code architecture.<br>


<p float="left">
  <img width="25%" height="45%" src="https://github.com/fsacchi/schoolmate/blob/main/screenshots/agenda.jpg" />
  <img width="25%" height="45%" src="https://github.com/fsacchi/schoolmate/blob/main/screenshots/discipline.jpg" />
  <img width="25%" height="45%" src="https://github.com/fsacchi/schoolmate/blob/main/screenshots/files.jpg" />
  <img width="25%" height="45%" src="https://github.com/fsacchi/schoolmate/blob/main/screenshots/edit_job.jpg" />
  <img width="25%" height="45%" src="https://github.com/fsacchi/schoolmate/blob/main/screenshots/calendar.jpg" />
  <img width="25%" height="45%" src="https://github.com/fsacchi/schoolmate/blob/main/screenshots/discipline_jobs.jpg" />
</p>

# Main Features
- Disciplines Register
- Manage the delivery of school activities 
- Possibility to upload files by discipline
- Easy to read delivery calendar
- Push notifications of upcoming deliveries

## Architecture 🏗️
  - MVVM Architecture (Model - View - ViewModel)
  - Clean Code with UseCases
  - Repository pattern
  - Koin - dependency injection

## Firebase Resources
  - Authentication
  - Firestore Database
  - Storage
  - Crashlytics

## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Jetpack Data Binding](https://developer.android.com/topic/libraries/data-binding?hl=pt-br) - Support library that lets you bind interface components in your layouts to data sources in your app
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [MutableSharedFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-mutable-shared-flow/) - Data objects that notify views when the underlying database changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
  - [UseCases](https://engineering.teknasyon.com/usecase-red-flags-and-best-practices-in-clean-architecture-76e2f6d921eb) - The use case is responsible for encapsulating the business logic for a single reusable task the system must perform.
- [Dependency Injection](https://developer.android.com/training/dependency-injection)
  - [Koin](https://insert-koin.io/) - Simple, lightweight, and smart dependency injection for Android, without annotations or generated code.
- [Emoji](https://github.com/vanniktech/Emoji) - Keyboard for selecting emojis.
- [Picasso](https://github.com/square/picasso) - A powerful image downloading and caching library for Android.
- [Firebase](https://firebase.google.com/)
  - [Firebase Authentication](https://firebase.google.com/docs/auth?hl=pt-br) - Authenticates users easily with email and password, ensuring secure sign-in and registration.
  - [Firebase Realtime Database](https://firebase.google.com/docs/database?hl=pt-br) - A NoSQL cloud database for storing and syncing data in real-time across all clients.
  - [Firebase Storage](https://firebase.google.com/docs/storage?hl=pt-br) - Stores and serves user-generated content like photos and documents.
  - [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics?hl=pt-br) - Real-time crash reporting tool that helps you track, prioritize, and fix stability issues in app.


# License
```
Copyright 2024 fsacchi (Felipe Meirelles Sacchi)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

