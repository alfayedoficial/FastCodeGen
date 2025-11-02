# FastCodeGen - Kotlin Code Generator Plugin

**Version:** 1.0.6  
**Release Date:** November 2, 2025  
**Author:** Ali Al-Shahat Ali

![Plugin Version](https://img.shields.io/badge/version-1.0.6-blue.svg)
![IntelliJ Platform](https://img.shields.io/badge/platform-IntelliJ%202024.2%2B-orange.svg)
![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-purple.svg)

## 🚀 Overview

FastCodeGen is a powerful IntelliJ IDEA plugin that accelerates Kotlin development by automatically generating boilerplate code for common architectural patterns. Perfect for developers working with MVI, Clean Architecture, and Repository patterns in Kotlin projects.

## ✨ What's New in 1.0.6

### 🎯 Settings System
- **⚙️ Settings Dialog** - Configure your base class paths
- **📁 Browse Buttons** - Auto-detect package paths from files
- **Dynamic Imports** - No more hardcoded paths!
- **User-Friendly** - Works with any project structure

### 🏗️ Refactored Architecture
- **Modular Design** - Clean separation of concerns
- **Panel System** - Organized UI components
- **Generation Manager** - Centralized business logic
- **80% Code Reduction** - Main dialog now ~150 lines (was 800+)

### 🔧 Enhanced Features
- **Optional Repository Methods** - Methods, params, and return types can be empty
- **K2 Compiler Support** - Full compatibility with Kotlin 2.1.0+
- **Improved Validation** - Settings validated before generation
- **Better UX** - Help menu, documentation access, and more

## 📋 Features

### Code Generation Types

#### 1. **ViewModel State**
Generate complete ViewModel with:
- State sealed class (Idle, Loading, Success, Error)
- Event sealed class (optional)
- UIState data class (optional, with Refreshable support)
- Intent sealed class
- ViewModel class with proper configuration

#### 2. **Repository**
Generate repository with:
- Repository interface with custom methods
- Repository implementation with HttpClient support (optional)
- Flow-based async operations
- Methods are fully optional

#### 3. **Full Feature**
Generate complete feature including:
- All ViewModel State components
- All Repository components
- Organized folder structure
- Proper imports and dependencies

### Configuration Options

- ✅ **Enable Events** - Add event handling to ViewModel
- ✅ **Enable Refresh** - Add refresh capability
- ✅ **Enable UIState** - Include UI state management
- ✅ **Use Cases** - Specify use case dependencies
- ✅ **HTTP Client** - Include Ktor HttpClient in repository
- ✅ **Custom Methods** - Define repository methods with params and return types

## 🎯 How to Use

### First Time Setup

1. **Open Settings**
   - Click the ⚙️ button in the FastCodeGen dialog, or
   - Go to **Tools → FastCodeGen Documentation → Settings**

2. **Configure Base Class Paths**
   - Click 📁 next to each field
   - Browse to your base class file (e.g., `AppViewModel.kt`)
   - Plugin auto-detects the full package path
   - Save settings

3. **Start Generating Code!**

### Generating Code

1. **Right-click** on any package/folder in your Kotlin project
2. Select **New → FastCodeGen**
3. Choose your generation type:
   - **ViewmodelState** - Generate only ViewModel State files
   - **Repo** - Generate only Repository files
   - **Feature** - Generate complete feature (ViewModel + Repository)
4. Configure your options
5. Click **Generate**

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

## 🔧 Requirements

- **IntelliJ IDEA 2024.2+** or **Android Studio Koala+**
- **Kotlin plugin** enabled
- **Kotlin project** with standard source structure
- **Base classes configured** in settings

## 📚 Documentation

Access comprehensive documentation from the plugin:
- 📖 **README** - Getting started guide
- 📚 **User Guide** - Detailed tutorials
- ⚡ **Quick Reference** - Cheat sheet
- 🔧 **Implementation Steps** - Setup instructions
- 📊 **Plugin Summary** - Feature overview

Or click the **📚 Help** button in the dialog.

## 🎨 Settings Configuration

### Required Base Classes

Configure the full package paths for:
- **AppViewModel** - Your base ViewModel class
- **ViewModelConfig** - Configuration class
- **BaseState** - Base state interface
- **BaseEvent** - Base event interface
- **BaseUIState** - Base UI state interface
- **Refreshable** - Refreshable interface
- **BaseIntent** - Base intent interface

### Optional Settings
- **Koin Module** - Dependency injection module path

## 💡 Example Usage

### Example 1: Login Feature
Generate a complete login feature with authentication:
```
Feature Name: Login
✓ Enable Events
✓ Enable Refresh
✓ Enable UIState
Use Cases: Authentication
```

**Generated Structure:**
```
login/
├── viewmodel/
│   ├── state/
│   │   └── LoginState.kt
│   └── LoginViewModel.kt
└── (repository if selected)
```

### Example 2: User Repository
Generate a user repository with custom methods:
```
Feature Name: User
Methods:
  - getUser(userId: String) → Flow<User>
  - updateUser(user: User) → Flow<Unit>
✓ Needs HttpClient
```

## 🏆 Benefits

### For Developers
- ⚡ **Save Time** - Generate code in seconds
- 🎯 **Consistency** - Follow best practices automatically
- 🔧 **Customizable** - Adapt to your project structure
- 📦 **Clean Code** - Well-organized, maintainable output

### For Teams
- 📐 **Standardization** - Same code structure across team
- 📚 **Onboarding** - New developers start faster
- 🔄 **Productivity** - Focus on business logic, not boilerplate
- ✅ **Quality** - Reduce human error

## 🛠️ Technical Details

### Generated Code Structure

**ViewModel:**
```kotlin
class LoginViewModel : AppViewModel<LoginState, LoginEvent, LoginUIState, LoginIntent>(
    initialState = LoginState.Idle,
    initialUIState = LoginUIState(),
    config = ViewModelConfig(
        enableRefresh = true,
        enableEvents = true
    )
) {
    override fun handleIntent(intent: LoginIntent) { }
    override fun createErrorState(message: String): LoginState { }
    override fun createErrorEvent(message: String): LoginEvent { }
}
```

**Repository:**
```kotlin
interface UserRepo {
    fun getUser(userId: String): Flow<User>
    fun updateUser(user: User): Flow<Unit>
}

class UserRepoImpl(
    private val httpClient: HttpClient
) : UserRepo {
    override fun getUser(userId: String): Flow<User> {
        TODO("Not yet implemented")
    }
}
```

### File Organization

```
com.yourapp.feature/
├── domain/
│   └── repo/
│       └── FeatureRepo.kt
├── data/
│   └── repo/
│       └── FeatureRepoImpl.kt
└── viewmodel/
    ├── state/
    │   └── FeatureState.kt
    └── FeatureViewModel.kt
```

## 🐛 Troubleshooting

### Settings Not Saved
- Make sure all required fields are filled
- Click OK to save (not Cancel)
- Restart IDE if needed

### Generated Code Has Errors
- Verify settings paths are correct
- Check that base classes exist in your project
- Ensure Kotlin plugin is enabled

### Plugin Not Appearing
- Check minimum IDE version (2024.2+)
- Verify Kotlin plugin is enabled
- Try invalidating caches: **File → Invalidate Caches → Restart**

## 📝 Changelog

### Version 1.0.6 (November 2, 2025)
- ✨ Added settings system with browse buttons
- 🎨 Refactored UI into modular panels
- 🔧 Dynamic imports based on user configuration
- 📁 Auto-detect package paths from files
- ✅ Optional repository methods and parameters
- 🏗️ Improved code organization (80% reduction)
- 📚 Enhanced documentation system
- ⚙️ Settings button in dialog
- 🔍 Better validation and error messages

### Version 1.0.0 (October 2024)
- 🎉 Initial release
- ViewModel State generation
- Repository generation
- Full Feature generation
- K2 compiler support

## 🤝 Contributing

Found a bug or have a feature request?
- Report issues on GitHub
- Contact: alialfayed.official@gmail.com

## 📄 License

Copyright © 2024-2025 Ali Al-Shahat Ali

## 🔗 Links

- **LinkedIn:** [alfayedoficial](https://www.linkedin.com/in/alfayedoficial/)
- **GitHub:** [alfayedoficial](https://github.com/alfayedoficial)
- **Email:** alialfayed.official@gmail.com

---

<div align="center">

**Made with ❤️ by Ali Al-Shahat Ali**

*Accelerate your Kotlin development with FastCodeGen*

</div>