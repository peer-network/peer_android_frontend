# Peer Android Frontend

The official Android client for Peer Network, built with modern Android development standards.

[![Contributing](https://img.shields.io/badge/Contributing-Guidelines-blue.svg)](https://github.com/peer-network/.github/blob/main/CONTRIBUTING.md)

## 🏗 Technology Stack

- **Language**: [Kotlin](https://kotlinlang.org/) (100%)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture**: 
  - [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
  - MVVM + MVI (State Pattern)
  - Multi-Module (Feature-driven)
- **Dependency Injection**: [Dagger 2](https://dagger.dev/) via direct Component construction
- **Asynchronicity**: [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) & [Flow](https://kotlinlang.org/docs/flow.html)
- **Data Loading**: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3)

## 📐 Architecture Overview

The project is highly modularized to ensure separation of concerns and scalability. Each high-level feature (e.g., `Social`, `User`, `Wallet`) is split into four distinct sub-modules:

### Layer Breakdown

1.  **`:domain`**
    *   **Responsibility**: Business logic, pure Kotlin.
    *   **Contents**: Use Cases, Repository Interfaces, Domain Models.
    *   **Dependencies**: None (or pure Java/Kotlin libraries).

2.  **`:data`**
    *   **Responsibility**: Data retrieval and management.
    *   **Contents**: Repository Implementations, API definitions, Data Sources.
    *   **Dependencies**: `:domain`, Networking libraries.

3.  **`:ui`**
    *   **Responsibility**: Rendering UI and handling user interaction.
    *   **Contents**: Jetpack Compose Screens, ViewModels, DI Components (`UiComponentProvider`).
    *   **Dependencies**: `:domain`, `:core:ui`.

4.  **`:remote`**
    *   **Responsibility**: Network communication specifics (DTOs, Services).

### Core Modules

-   **`:core:common`**: Shared utilities, extensions, and base classes (e.g., `Result` types).
-   **`:core:ui`**: Design System (`Luna`), shared Composables, and Theme definitions.

## 📂 Project Structure

```text
peer_android_frontend/
├── app/                  # Main Application module (DI root, manifest)
├── core/
│   ├── common/           # Utils
│   └── ui/               # Design System and base UI components
├── social/               # Social features (Feed, Connections)
│   ├── domain/
│   ├── data/
│   └── ui/
├── user/                 # User management (Auth, Profile)
├── wallet/               # Crypto wallet features
├── messaging/            # Chat features
└── ...
```

## 🛠 Usage & patterns

### UI Component Hierarchy
The UI acts as a composition of three distinct layers:
1.  **Stateful Components (Screens)**: Composition roots (e.g., `LoginScreen`) responsible for Dependency Injection, ViewModel creation, and state collection.
2.  **Stateless Components**: Complex UI compositions (e.g., `LoginPage`) that define the layout and behavioral contracts. They are pure functions receiving state and event callbacks.
3.  **View Components**: Reusable, atomic building blocks (e.g., `ConnectionButton`, `DesignOutlinedButton`) shared across the application.

## 🚀 Build & Setup

1.  **Prerequisites**:
    *   Android Studio Iguana or later.
    *   JDK 17.

2.  **Build**:
    ```bash
    ./gradlew assembleDebug
    ```

3.  **Run Tests**:
    ```bash
    ./gradlew test
    ```