# FastCodeGen - Kotlin Code Generator Plugin

[![Version](https://img.shields.io/badge/version-1.0.6-blue.svg)](https://github.com/alfayedoficial/FastCodeGen)
[![IntelliJ Plugin](https://img.shields.io/badge/IntelliJ-Plugin-orange.svg)](https://plugins.jetbrains.com/plugin/fastcodegen)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0+-purple.svg)](https://kotlinlang.org/)

**FastCodeGen** is a powerful IntelliJ IDEA / Android Studio plugin that accelerates Kotlin development by automatically generating boilerplate code for ViewModels, Repositories, Compose Screens, and complete features following clean architecture principles.

---

## 🎯 What's New in Version 1.0.6

### 🎨 Screen Generation (NEW!)
- **Jetpack Compose Screen Generation** - Automatically create complete Compose screens
- **Navigation Integration** - Built-in support for multiple navigation types:
   - **None** - Simple screen without navigation
   - **Simple** - Using `composableRoute` for standard navigation
   - **Type-Safe** - Using `composableSafeType` with `@Serializable`
- **Navigation Parameters** - Define and inject navigation parameters automatically
- **ViewModel Integration** - Auto-wire ViewModels with state collection

### ⚙️ Enhanced Settings System
- **Browse & Auto-Detect** - Click 📁 to browse files and auto-detect package paths
- **Dynamic Imports** - All imports are now based on your configuration
- **Navigation Utilities** - Configure `composableRoute` and `composableSafeType` paths
- **Validation** - Settings are validated before code generation

### 🏗️ Architecture Improvements
- **Modular Panel System** - Clean separation of UI components
- **Generation Manager** - Centralized business logic
- **80% Code Reduction** - Main dialog simplified from 800+ to ~150 lines
- **StringUtils** - Built-in utilities for case conversion

### 🔧 Feature Enhancements
- **Include Load Method** - Optional `load()` method for ViewModel initialization
- **Optional Repository Methods** - Methods, parameters, and return types can be empty
- **Improved Validation** - Better error messages and validation
- **Enhanced UX** - Settings button in dialog, help menu, documentation access

---

## 📋 Features

### Code Generation Types

#### 1. **ViewModel State**
Generate complete ViewModel with:
- State sealed class (Idle, Loading, Success, Error)
- Event sealed class (optional)
- UIState data class (optional, with Refreshable support)
- Intent sealed class
- ViewModel class with proper configuration
- Optional `load()` method for initialization

#### 2. **Repository**
Generate repository with:
- Repository interface with custom methods
- Repository implementation with HttpClient support (optional)
- Flow-based async operations
- Fully optional methods, parameters, and return types

#### 3. **Compose Screen** (NEW!)
Generate Jetpack Compose screen with:
- Complete Composable function structure
- Navigation parameter support (Simple or Type-Safe)
- Optional ViewModel integration with state collection
- Navigation back support
- LaunchedEffect for initialization
- State handling (Loading, Success, Error, Idle)

#### 4. **Full Feature**
Generate complete feature including:
- Compose Screen (optional)
- ViewModel State components (optional)
- Repository components (optional)
- Organized folder structure
- Proper imports and dependencies

### Configuration Options

- ✅ **Generate Screen** - Create Jetpack Compose screen
- ✅ **Generate ViewModel** - Include ViewModel State files
- ✅ **Generate Repository** - Include Repository files
- ✅ **Enable Events** - Add event handling to ViewModel
- ✅ **Enable Refresh** - Add refresh capability
- ✅ **Enable UIState** - Include UI state management
- ✅ **Include Load Method** - Add initialization method to ViewModel
- ✅ **Navigation Type** - Choose navigation approach (None/Simple/Type-Safe)
- ✅ **Navigation Parameters** - Define parameters for type-safe navigation
- ✅ **Use Cases** - Specify use case dependencies
- ✅ **HTTP Client** - Include Ktor HttpClient in repository
- ✅ **Custom Methods** - Define repository methods with optional params and return types

---

## 🚀 Quick Start

### First-Time Setup

1. **Install the Plugin**
   - Open Settings → Plugins → Marketplace
   - Search for "FastCodeGen"
   - Click Install and restart IDE

2. **Configure Settings** (One-time setup)
   - Right-click on any package → New → FastCodeGen
   - Click ⚙️ Settings button
   - Configure all required paths:
      - Click 📁 next to each field
      - Browse to your base class file (e.g., `AppViewModel.kt`)
      - Plugin auto-detects the full package path
   - Click OK to save

3. **Start Generating!**
   - Right-click on target package
   - Select New → FastCodeGen
   - Choose generation type and configure options

---

## 📦 Installation

### From JetBrains Marketplace
1. Open **Settings/Preferences** → **Plugins**
2. Search for **"FastCodeGen"**
3. Click **Install**
4. Restart IDE

### Manual Installation
1. Download the plugin `.zip` from releases
2. **Settings/Preferences** → **Plugins** → **⚙️** → **Install Plugin from Disk**
3. Select the downloaded file
4. Restart IDE

---

## 🎯 Usage Guide

### Generating a Complete Feature

**Example: Create a Login Feature with Screen, ViewModel, and Repository**

1. Right-click on your feature package
2. Select **New → FastCodeGen**
3. Select **Full Feature**
4. Click **Next →**
5. Configure:
   ```
   Feature Name: Login
   
   Generation Options:
   ✅ Generate Screen
   ✅ Generate ViewModel
   ✅ Generate Repository
   
   Screen Configuration:
   ✅ Has Navigation Back
   Navigation Type: Type-Safe
   Parameters:
     - email: String
     - fromSignup: Boolean
   
   ViewModel Configuration:
   ✅ Enable Events
   ✅ Enable Refresh
   ✅ Enable UIState
   ✅ Include Load Method
   Use Cases: AuthenticationUseCase
   
   Repository Configuration:
   ✅ Needs HttpClient
   Methods:
     - login(email: String, password: String) → Flow<User>
     - validateSession() → Flow<Boolean>
   ```
6. Click **Generate**

**Generated Structure:**
```
login/
├── ui/
│   └── LoginScreen.kt
├── viewmodel/
│   ├── state/
│   │   ├── LoginState.kt
│   │   ├── LoginEvent.kt
│   │   ├── LoginUIState.kt
│   │   └── LoginIntent.kt
│   └── LoginViewModel.kt
├── domain/
│   └── repo/
│       └── LoginRepo.kt
└── data/
    └── repo/
        └── LoginRepoImpl.kt
```

### Generating Individual Components

#### Screen Only
```
Generation Type: Screen
Feature Name: Profile
✅ Has Navigation Back
Navigation Type: Simple
```

#### ViewModel Only
```
Generation Type: ViewModel State
Feature Name: Dashboard
✅ Enable Events
✅ Include Load Method
Use Cases: GetStatsUseCase
```

#### Repository Only
```
Generation Type: Repository
Feature Name: User
✅ Needs HttpClient
Methods:
  - getUser(id: String) → Flow<User>
  - updateProfile(user: User) → Flow<Unit>
```

---

## 🔧 Requirements

- **IntelliJ IDEA 2024.2+** or **Android Studio Koala+**
- **Kotlin plugin** enabled
- **Kotlin project** with standard source structure
- **Base classes** configured in settings
- **Jetpack Compose** (optional, for screen generation)
- **Navigation Compose** (optional, for navigation features)

---

## ⚙️ Settings Configuration

### Required Base Classes

Configure the full package paths for:

| Setting | Description | Example |
|---------|-------------|---------|
| **AppViewModel** | Your base ViewModel class | `com.myapp.core.viewmodel.AppViewModel` |
| **ViewModelConfig** | Configuration class | `com.myapp.core.viewmodel.ViewModelConfig` |
| **BaseState** | Base state interface | `com.myapp.core.viewmodel.BaseState` |
| **BaseEvent** | Base event interface | `com.myapp.core.viewmodel.BaseEvent` |
| **BaseUIState** | Base UI state interface | `com.myapp.core.viewmodel.BaseUIState` |
| **Refreshable** | Refreshable interface | `com.myapp.core.viewmodel.Refreshable` |
| **BaseIntent** | Base intent interface | `com.myapp.core.viewmodel.BaseIntent` |

### Required Navigation Utilities

| Setting | Description | Example |
|---------|-------------|---------|
| **composableRoute** | Function for simple navigation | `com.myapp.core.utilities.composableRoute` |
| **composableSafeType** | Function for type-safe navigation | `com.myapp.core.utilities.composableSafeType` |

### Optional Settings

| Setting | Description | Example |
|---------|-------------|---------|
| **Koin Module** | Dependency injection module | `org.koin.core.module.Module` |

---

## 💡 Code Examples

### Generated Screen with Type-Safe Navigation

```kotlin
package com.myapp.profile.ui

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myapp.profile.viewmodel.ProfileIntent
import com.myapp.profile.viewmodel.ProfileViewModel
import com.myapp.profile.viewmodel.state.ProfileState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    userId: String,
    fromSettings: Boolean,
    navigationBack: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProfileIntent.LoadProfile(userId))
    }

    when (val currentState = state) {
        is ProfileState.Loading -> LoadingContent()
        is ProfileState.Success -> ProfileContent(
            data = currentState.data,
            uiState = uiState,
            onIntent = viewModel::handleIntent
        )
        is ProfileState.Error -> ErrorContent(
            message = currentState.message,
            onRetry = { viewModel.handleIntent(ProfileIntent.LoadProfile(userId)) }
        )
        ProfileState.Idle -> IdleContent()
    }
}
```

### Generated ViewModel with Load Method

```kotlin
package com.myapp.login.viewmodel

import com.myapp.core.viewmodel.AppViewModel
import com.myapp.core.viewmodel.ViewModelConfig
import com.myapp.login.viewmodel.state.LoginIntent
import com.myapp.login.viewmodel.state.LoginState
import com.myapp.login.viewmodel.state.LoginEvent
import com.myapp.login.viewmodel.state.LoginUIState

class LoginViewModel(
    private val authenticationUseCase: AuthenticationUseCase
) : AppViewModel<LoginState, LoginEvent, LoginUIState, LoginIntent>(
    initialState = LoginState.Idle,
    initialUIState = LoginUIState(),
    config = ViewModelConfig(
        enableRefresh = true,
        enableEvents = true
    )
) {

    init {
        loadLogin()
    }

    override fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.ClearState -> setState(LoginState.Idle)
            is LoginIntent.LoadLogin -> loadLogin()
            is LoginIntent.RefreshRequest -> refreshRequest { loadLogin() }
        }
    }

    override fun createErrorState(message: String): LoginState {
        return LoginState.Error(message)
    }

    override fun createErrorEvent(message: String): LoginEvent {
        return LoginEvent.Error(message)
    }

    private fun loadLogin() {
        launch {
            setState(LoginState.Loading)
            // TODO: Implement
        }
    }
}
```

### Generated Repository

```kotlin
// Interface
package com.myapp.user.domain.repo

import kotlinx.coroutines.flow.Flow

interface UserRepo {
    fun getUser(userId: String): Flow<User>
    fun updateUser(user: User): Flow<Unit>
    fun deleteUser(userId: String): Flow<Boolean>
}

// Implementation
package com.myapp.user.data.repo

import com.myapp.user.domain.repo.UserRepo
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

class UserRepoImpl(
    private val httpClient: HttpClient,
) : UserRepo {

    override fun getUser(userId: String): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: User): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteUser(userId: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}
```

---

## 🏆 Benefits

### For Developers
- ⚡ **Save Time** - Generate complete features in seconds
- 🎯 **Consistency** - Follow best practices automatically
- 🔧 **Customizable** - Adapt to your project structure
- 📦 **Clean Code** - Well-organized, maintainable output
- 🎨 **Modern Stack** - Built for Compose and modern Android

### For Teams
- 📐 **Standardization** - Same code structure across team
- 📚 **Onboarding** - New developers start faster
- 🔄 **Productivity** - Focus on business logic, not boilerplate
- ✅ **Quality** - Reduce human error

---

## 🐛 Troubleshooting

### Settings Not Saved
**Solution:**
- Ensure all required fields are filled
- Click OK (not Cancel)
- Restart IDE if needed

### Generated Code Has Errors
**Solution:**
- Verify settings paths are correct using ⚙️ button
- Check that base classes exist in your project
- Ensure Kotlin plugin is enabled
- For screens, verify Compose dependencies are added

### Plugin Not Appearing
**Solution:**
- Check minimum IDE version (2024.2+)
- Verify Kotlin plugin is enabled
- Try: File → Invalidate Caches → Restart

### Screen Generation Issues
**Solution:**
- Ensure Jetpack Compose dependencies are in build.gradle
- Verify navigation utilities paths in settings
- Check that `composableRoute` or `composableSafeType` exist

### Navigation Not Working
**Solution:**
- For Simple navigation: Configure `composableRoute` path
- For Type-Safe navigation: Configure `composableSafeType` path
- Ensure navigation utility functions match your project structure

---

## 📚 Documentation

Access comprehensive documentation from the plugin:
- 📖 **README** - This getting started guide
- 📚 **User Guide** - Detailed tutorials and examples
- ⚡ **Quick Reference** - Cheat sheet for quick lookup
- 🔧 **Implementation Steps** - Setup instructions
- 📊 **Plugin Summary** - Feature overview
- 📑 **Complete Index** - All documentation

Click the **📚 Help** button in the dialog to access these resources.

---

## 📝 Changelog

### Version 1.0.6 (November 2025)

#### 🎨 Major Features
- ✨ **Screen Generation** - Full Jetpack Compose screen generation with navigation
- 🧭 **Navigation Integration** - Support for None, Simple, and Type-Safe navigation
- 🔒 **Type-Safe Navigation** - Automatic parameter injection with `@Serializable`
- 🎯 **ViewModel Integration** - Auto-wire ViewModels with screens

#### 🏗️ Architecture & Refactoring
- 🎨 Refactored UI into modular panel system
- 🔧 Introduced `GenerationManager` for centralized logic
- 📦 80% code reduction in main dialog (800+ → ~150 lines)
- 🛠️ Added `StringUtils` utility (toCamelCase, toPascalCase, toSnakeCase)

#### ⚙️ Settings & Configuration
- ✨ Comprehensive settings dialog with browse buttons
- 📁 Auto-detect package paths from selected files
- 🔧 Dynamic imports based on user configuration
- 🧭 Navigation utilities configuration
- ✅ Settings validation before generation
- 🔧 Fixed file chooser implementation

#### 🔧 Enhanced Features
- ✅ **Include Load Method** - Optional initialization in ViewModels
- 📝 Optional repository methods (fully supports empty methods)
- 🎨 Enhanced `FeatureGenerator` with optional Screen creation
- 🔄 Improved parameter injection in screen generation
- 📚 Enhanced documentation system
- ⚙️ Settings button in main dialog
- 🔍 Better validation and error messages

#### 🐛 Bug Fixes
- Fixed file chooser behavior in settings dialog
- Improved path detection and validation
- Enhanced error handling in generation process

### Version 1.0.0 (October 2025)
- 🎉 Initial release
- ViewModel State generation
- Repository generation
- Full Feature generation
- K2 Compiler Support

---

## 🤝 Contributing

Found a bug or have a feature request?
- 🐛 **Report Issues**: [GitHub Issues](https://github.com/alfayedoficial/FastCodeGen/issues)
- 💬 **Discussions**: [GitHub Discussions](https://github.com/alfayedoficial/FastCodeGen/discussions)
- 📧 **Email**: alialfayed.official@gmail.com

---

## 📄 License

Copyright © 2024-2025 Ali Al-Shahat Ali

---

## 🔗 Links

- **GitHub**: [alfayedoficial/FastCodeGen](https://github.com/alfayedoficial)
- **LinkedIn**: [alfayedoficial](https://www.linkedin.com/in/alfayedoficial/)
- **Email**: alialfayed.official@gmail.com

---

<div align="center">

**Made with ❤️ by Ali Al-Shahat Ali**

*Accelerate your Kotlin development with FastCodeGen*

⭐ Star us on GitHub | 💬 Join the Discussion | 🐛 Report Issues

</div>