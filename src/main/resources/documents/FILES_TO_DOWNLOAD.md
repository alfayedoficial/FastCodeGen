# FastCodeGen - Files to Download

**Version 1.0.6 - Required and Optional Files**

---

## Overview

This document lists all files you need to download, create, or configure to use FastCodeGen effectively. Files are organized by category and priority.

---

## 📋 Quick Checklist

### Required Files (Must Have)
- [ ] Base Interfaces (BaseState, BaseEvent, BaseUIState, BaseIntent, Refreshable)
- [ ] ViewModelConfig
- [ ] AppViewModel
- [ ] Navigation Utilities (composableRoute, composableSafeType)

### Optional Files (Recommended)
- [ ] Dependency Injection Setup (Koin or Hilt)
- [ ] HTTP Client Configuration
- [ ] Example Use Cases

---

## 🔴 Required Base Classes

### 1. Base Interfaces File

**Location:** `core/viewmodel/BaseInterfaces.kt`

**Contents:**
```kotlin
package com.yourapp.core.viewmodel

/**
 * Base interface for all states
 */
interface BaseState

/**
 * Base interface for all events
 */
interface BaseEvent

/**
 * Special event type for ViewModels without events
 */
object NoEvent : BaseEvent

/**
 * Base interface for all UI states
 */
interface BaseUIState

/**
 * Base interface for all intents
 */
interface BaseIntent

/**
 * Interface for refreshable UI states
 */
interface Refreshable {
    fun withRefresh(isRefresh: Boolean): BaseUIState
}
```

**Purpose:**
- Defines contracts for state management
- Used by all generated ViewModels
- Ensures type safety

### 2. ViewModelConfig File

**Location:** `core/viewmodel/ViewModelConfig.kt`

**Contents:**
```kotlin
package com.yourapp.core.viewmodel

/**
 * Configuration for ViewModel behavior
 */
data class ViewModelConfig(
    val enableRefresh: Boolean = false,
    val enableEvents: Boolean = false
)
```

**Purpose:**
- Configures ViewModel features
- Controls refresh and event behavior

### 3. AppViewModel File

**Location:** `core/viewmodel/AppViewModel.kt`

**Contents:**
```kotlin
package com.yourapp.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Base ViewModel with state management
 */
abstract class AppViewModel<STATE : BaseState, EVENT : BaseEvent, UISTATE : BaseUIState, INTENT : BaseIntent>(
    initialState: STATE,
    initialUIState: UISTATE,
    private val config: ViewModelConfig
) : ViewModel() {

    // State management
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    // UI State management
    private val _uiState = MutableStateFlow(initialUIState)
    val uiState: StateFlow<UISTATE> = _uiState.asStateFlow()

    // Event management (if enabled)
    private val _event = MutableSharedFlow<EVENT>()
    val event: SharedFlow<EVENT> = _event.asSharedFlow()

    // Error handler
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    /**
     * Handle intents from UI
     */
    abstract fun handleIntent(intent: INTENT)

    /**
     * Create error state from exception
     */
    abstract fun createErrorState(message: String): STATE

    /**
     * Create error event from exception (only if events are enabled)
     */
    open fun createErrorEvent(message: String): EVENT {
        throw NotImplementedError("createErrorEvent must be implemented when events are enabled")
    }

    /**
     * Update state
     */
    protected fun setState(state: STATE) {
        _state.value = state
    }

    /**
     * Update UI state
     */
    protected fun setUIState(uiState: UISTATE) {
        _uiState.value = uiState
    }

    /**
     * Emit event (only if events are enabled)
     */
    protected suspend fun emitEvent(event: EVENT) {
        if (config.enableEvents) {
            _event.emit(event)
        }
    }

    /**
     * Launch coroutine with error handling
     */
    protected fun launch(block: suspend () -> Unit) {
        viewModelScope.launch(errorHandler) {
            block()
        }
    }

    /**
     * Handle refresh request (only if refresh is enabled)
     */
    protected fun refreshRequest(onRefresh: () -> Unit) {
        if (config.enableRefresh && _uiState.value is Refreshable) {
            val refreshable = _uiState.value as Refreshable
            setUIState(refreshable.withRefresh(true) as UISTATE)
            onRefresh()
            viewModelScope.launch {
                setUIState(refreshable.withRefresh(false) as UISTATE)
            }
        }
    }

    /**
     * Handle errors
     */
    private fun handleError(exception: Throwable) {
        val message = exception.message ?: "Unknown error occurred"
        
        // Update state
        setState(createErrorState(message))
        
        // Emit error event if enabled
        if (config.enableEvents) {
            viewModelScope.launch {
                emitEvent(createErrorEvent(message))
            }
        }
    }
}
```

**Purpose:**
- Base class for all ViewModels
- Handles state, events, and errors
- Provides lifecycle management

### 4. Navigation Utilities File

**Location:** `core/utilities/NavigationUtils.kt`

**Contents:**
```kotlin
package com.yourapp.core.utilities

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

/**
 * Simple navigation wrapper using route constants
 */
@Composable
fun composableRoute(
    navigationBack: (() -> Unit)? = null,
    enableBackHandler: Boolean = true,
    content: @Composable () -> Unit
) {
    // Handle back press
    if (navigationBack != null && enableBackHandler) {
        BackHandler {
            navigationBack()
        }
    }
    
    content()
}

/**
 * Type-safe navigation wrapper using @Serializable routes
 */
@Composable
fun composableSafeType(
    navigationBack: (() -> Unit)? = null,
    enableBackHandler: Boolean = true,
    content: @Composable () -> Unit
) {
    // Handle back press
    if (navigationBack != null && enableBackHandler) {
        BackHandler {
            navigationBack()
        }
    }
    
    // Add any type-safe navigation specific logic
    
    content()
}
```

**Purpose:**
- Navigation wrappers for Compose screens
- Handles back press
- Supports type-safe navigation

---

## 🟡 Optional but Recommended

### 5. Koin Module Template

**Location:** `di/AppModule.kt`

**Contents:**
```kotlin
package com.yourapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // ViewModels will be registered here
    // Example:
    // viewModel { LoginViewModel(get()) }
}

val useCaseModule = module {
    // Use Cases will be registered here
    // Example:
    // single { AuthenticateUseCase(get()) }
}

val repositoryModule = module {
    // Repositories will be registered here
    // Example:
    // single<LoginRepo> { LoginRepoImpl(get()) }
}

val networkModule = module {
    // Network components
    // Example:
    // single { HttpClient { /* config */ } }
}
```

**Purpose:**
- Dependency injection setup
- Module organization
- Easy registration of generated components

### 6. Application Class

**Location:** `App.kt` (root package)

**Contents:**
```kotlin
package com.yourapp

import android.app.Application
import com.yourapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@App)
            modules(
                viewModelModule,
                useCaseModule,
                repositoryModule,
                networkModule
            )
        }
    }
}
```

**Purpose:**
- Initialize dependency injection
- App-level configuration

### 7. HTTP Client Configuration

**Location:** `core/network/HttpClientConfig.kt`

**Contents:**
```kotlin
package com.yourapp.core.network

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun createHttpClient(): HttpClient {
    return HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }
}
```

**Purpose:**
- Centralized HTTP client configuration
- Used by generated repositories

---

## 🟢 Example Files (For Reference)

### 8. Example Use Case

**Location:** `domain/usecase/ExampleUseCase.kt`

**Contents:**
```kotlin
package com.yourapp.domain.usecase

import com.yourapp.domain.repo.ExampleRepo
import kotlinx.coroutines.flow.Flow

class ExampleUseCase(
    private val repository: ExampleRepo
) {
    operator fun invoke(param: String): Flow<Result> {
        return repository.getData(param)
    }
}
```

**Purpose:**
- Template for creating use cases
- Shows proper structure

### 9. Example Model

**Location:** `domain/model/ExampleModel.kt`

**Contents:**
```kotlin
package com.yourapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)
```

**Purpose:**
- Example domain models
- Serialization setup

---

## 📦 Dependencies

### Required Dependencies

Add to `build.gradle.kts` (app module):

```kotlin
dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
}
```

### Optional Dependencies

```kotlin
dependencies {
    // Jetpack Compose (for screen generation)
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.activity:activity-compose:1.8.0")
    
    // Koin (for dependency injection)
    implementation("io.insert-koin:koin-android:3.5.0")
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")
    
    // Ktor (for HTTP client in repositories)
    implementation("io.ktor:ktor-client-core:2.3.5")
    implementation("io.ktor:ktor-client-android:2.3.5")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
    implementation("io.ktor:ktor-client-logging:2.3.5")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
    
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}
```

### Gradle Plugins

Add to `build.gradle.kts` (project level):

```kotlin
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20" apply false
}
```

---

## 📋 File Checklist with Priority

### Priority 1: Absolutely Required
```
✅ core/viewmodel/BaseInterfaces.kt
✅ core/viewmodel/ViewModelConfig.kt
✅ core/viewmodel/AppViewModel.kt
✅ core/utilities/NavigationUtils.kt
```

**Without these, the plugin cannot generate code.**

### Priority 2: Highly Recommended
```
✅ di/AppModule.kt (or equivalent DI setup)
✅ App.kt (Application class)
✅ build.gradle.kts (with required dependencies)
```

**Without these, generated code won't integrate properly.**

### Priority 3: Optional but Useful
```
☐ core/network/HttpClientConfig.kt
☐ domain/usecase/ExampleUseCase.kt (template)
☐ domain/model/ExampleModel.kt (template)
```

**These help with consistency but aren't strictly required.**

---

## 🔄 Download Methods

### Method 1: Copy from Documentation
1. Copy code from this document
2. Create files in your project
3. Adjust package names to match your project

### Method 2: Use Templates (if provided)
1. Download template files from GitHub
2. Place in correct locations
3. Update package names

### Method 3: Create from Scratch
1. Follow structure in this document
2. Implement interfaces and classes
3. Customize for your needs

---

## ✅ Verification Steps

After downloading/creating all files:

### 1. Check File Structure
```
com.yourapp/
├── core/
│   ├── viewmodel/
│   │   ├── BaseInterfaces.kt     ✅
│   │   ├── ViewModelConfig.kt    ✅
│   │   └── AppViewModel.kt       ✅
│   └── utilities/
│       └── NavigationUtils.kt    ✅
└── di/
    └── AppModule.kt              ✅
```

### 2. Verify Compilation
- Build project: `./gradlew build`
- No compilation errors
- All imports resolved

### 3. Test Plugin Configuration
1. Open FastCodeGen dialog
2. Click ⚙️ Settings
3. Browse to each file
4. Verify paths are detected correctly

### 4. Generate Test Feature
1. Create test package
2. Generate simple ViewModel
3. Verify generated code compiles
4. Check imports are correct

---

## 🆘 Common Issues

### Issue: "Cannot find BaseState"
**Solution:** Ensure `BaseInterfaces.kt` is in correct location with correct package name

### Issue: "AppViewModel not found"
**Solution:**
1. Check `AppViewModel.kt` exists
2. Verify package name matches your project
3. Sync Gradle files

### Issue: "Navigation utilities not found"
**Solution:**
1. Create `NavigationUtils.kt`
2. Add both `composableRoute` and `composableSafeType` functions
3. Configure paths in plugin settings

### Issue: "Dependencies not resolved"
**Solution:**
1. Add all required dependencies to `build.gradle.kts`
2. Sync Gradle
3. Clean and rebuild project

---

## 📚 Next Steps

After downloading all required files:

1. **Configure Plugin Settings**
   - Open Settings (⚙️)
   - Browse to each file
   - Save configuration

2. **Test Generation**
   - Generate test ViewModel
   - Verify output
   - Fix any issues

3. **Start Using**
   - Generate real features
   - Integrate with your app
   - Share with team

---

## 📞 Need Help?

If you have trouble with any files:
- 📧 Email: alialfayed.official@gmail.com
- 💬 GitHub: Report issue with file questions
- 📖 Check: [Implementation Steps](IMPLEMENTATION_STEPS.md) for detailed setup

---

## 🔗 Related Documentation

- [README](README.md) - Plugin overview
- [Implementation Steps](IMPLEMENTATION_STEPS.md) - Detailed setup guide
- [User Guide](USER_GUIDE.md) - Usage tutorials
- [Quick Reference](QUICK_REFERENCE.md) - Syntax cheat sheet

---

**All Required Files Listed - Ready to Download! 📁**

*Last Updated: November 2025 - Version 1.0.6*