# FastCodeGen Implementation Steps

**Complete Setup Guide for Version 1.0.6**

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Project Setup](#project-setup)
3. [Base Classes Implementation](#base-classes-implementation)
4. [Navigation Utilities Setup](#navigation-utilities-setup)
5. [Plugin Configuration](#plugin-configuration)
6. [Dependency Injection Setup](#dependency-injection-setup)
7. [First Code Generation](#first-code-generation)
8. [Verification](#verification)

---

## Prerequisites

### IDE Requirements
- **IntelliJ IDEA 2024.2+** or **Android Studio Koala (2024.1.1)+**
- **Kotlin plugin** enabled (usually pre-installed)

### Project Requirements
- **Kotlin 1.9.0+** (Kotlin 2.0+ recommended)
- **Gradle 8.0+**
- **JDK 17+**

### Optional Dependencies
- **Jetpack Compose** (for screen generation)
- **Navigation Compose** (for navigation features)
- **Koin** or **Hilt** (for dependency injection)
- **Ktor Client** (for HTTP repositories)

---

## Project Setup

### Step 1: Add Required Dependencies

Add to your `build.gradle.kts` (app module):

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
    
    // Optional: Jetpack Compose (for screen generation)
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    
    // Optional: Dependency Injection (Koin)
    implementation("io.insert-koin:koin-android:3.5.0")
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")
    
    // Optional: HTTP Client (Ktor)
    implementation("io.ktor:ktor-client-core:2.3.5")
    implementation("io.ktor:ktor-client-android:2.3.5")
}
```

### Step 2: Create Core Package Structure

Create the following package structure in your project:

```
com.yourapp/
├── core/
│   ├── viewmodel/           # Base ViewModel classes
│   └── utilities/           # Navigation utilities
├── features/                # Feature modules
└── di/                      # Dependency injection
```

---

## Base Classes Implementation

### Step 1: Create Base Interfaces

Create `core/viewmodel/BaseInterfaces.kt`:

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

### Step 2: Create ViewModel Configuration

Create `core/viewmodel/ViewModelConfig.kt`:

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

### Step 3: Create Base ViewModel

Create `core/viewmodel/AppViewModel.kt`:

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

---

## Navigation Utilities Setup

### Option 1: Simple Navigation (Route-based)

Create `core/utilities/NavigationUtils.kt`:

```kotlin
package com.yourapp.core.utilities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Simple navigation wrapper using route constants
 */
@Composable
fun composableRoute(
    navigationBack: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    // You can add common navigation logic here
    // For example, back press handling
    
    content()
}
```

### Option 2: Type-Safe Navigation (Serializable-based)

If using Navigation Compose 2.8.0+:

```kotlin
package com.yourapp.core.utilities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Type-safe navigation wrapper using @Serializable routes
 */
@Composable
fun composableSafeType(
    navigationBack: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    // You can add common navigation logic here
    // For example, validation, analytics, etc.
    
    content()
}
```

### Complete Navigation Example

For a more complete navigation setup:

```kotlin
package com.yourapp.core.utilities

import androidx.compose.runtime.*
import androidx.activity.compose.BackHandler

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

---

## Plugin Configuration

### Step 1: Install Plugin

1. Open **Settings/Preferences** → **Plugins**
2. Search for **"FastCodeGen"**
3. Click **Install**
4. Restart IDE

### Step 2: Configure Settings

1. Right-click on any package in your project
2. Select **New → FastCodeGen**
3. Click the **⚙️ Settings** button

### Step 3: Configure Base Class Paths

For each field, click the **📁 browse button** and select the corresponding file:

**ViewModel Base Classes:**
1. **AppViewModel**: Browse to `core/viewmodel/AppViewModel.kt`
2. **ViewModelConfig**: Browse to `core/viewmodel/ViewModelConfig.kt`
3. **BaseState**: Browse to `core/viewmodel/BaseInterfaces.kt` (BaseState interface)
4. **BaseEvent**: Browse to `core/viewmodel/BaseInterfaces.kt` (BaseEvent interface)
5. **BaseUIState**: Browse to `core/viewmodel/BaseInterfaces.kt` (BaseUIState interface)
6. **Refreshable**: Browse to `core/viewmodel/BaseInterfaces.kt` (Refreshable interface)
7. **BaseIntent**: Browse to `core/viewmodel/BaseInterfaces.kt` (BaseIntent interface)

**Navigation Utilities:**
8. **composableRoute**: Browse to `core/utilities/NavigationUtils.kt`
9. **composableSafeType**: Browse to `core/utilities/NavigationUtils.kt`

**Optional:**
10. **Koin Module**: Leave empty or configure if using Koin

### Step 4: Verify Configuration

After browsing, your settings should look like:

```
AppViewModel:       com.yourapp.core.viewmodel.AppViewModel
ViewModelConfig:    com.yourapp.core.viewmodel.ViewModelConfig
BaseState:          com.yourapp.core.viewmodel.BaseState
BaseEvent:          com.yourapp.core.viewmodel.BaseEvent
BaseUIState:        com.yourapp.core.viewmodel.BaseUIState
Refreshable:        com.yourapp.core.viewmodel.Refreshable
BaseIntent:         com.yourapp.core.viewmodel.BaseIntent
composableRoute:    com.yourapp.core.utilities.composableRoute
composableSafeType: com.yourapp.core.utilities.composableSafeType
```

Click **OK** to save.

---

## Dependency Injection Setup

### Option 1: Koin Setup

#### Add Koin Dependencies

```kotlin
dependencies {
    implementation("io.insert-koin:koin-android:3.5.0")
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")
}
```

#### Create Koin Module

Create `di/AppModule.kt`:

```kotlin
package com.yourapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
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
```

#### Initialize Koin

In your `Application` class:

```kotlin
package com.yourapp

import android.app.Application
import com.yourapp.di.appModule
import com.yourapp.di.repositoryModule
import com.yourapp.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                useCaseModule,
                repositoryModule
            )
        }
    }
}
```

### Option 2: Hilt Setup

#### Add Hilt Dependencies

```kotlin
plugins {
    id("com.google.dagger.hilt.android") version "2.48"
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
}
```

#### Create Hilt Modules

```kotlin
package com.yourapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Provide dependencies here
}
```

---

## First Code Generation

### Test 1: Generate Simple ViewModel

1. Create a test package: `com.yourapp.features.test`
2. Right-click on the package
3. Select **New → FastCodeGen → ViewModel State**
4. Configure:
   ```
   Feature Name: Test
   ☐ Enable Events
   ☐ Enable Refresh
   ☐ Enable UIState
   ☐ Include Load Method
   Use Cases: (empty)
   ```
5. Click **Generate**

**Expected Result:**
- Files created in `test/viewmodel/`
- No compilation errors
- All imports resolved correctly

### Test 2: Generate Simple Screen

1. Right-click on the test package
2. Select **New → FastCodeGen → Screen**
3. Configure:
   ```
   Feature Name: TestScreen
   ☐ Has Navigation Back
   Navigation Type: None
   ```
4. Click **Generate**

**Expected Result:**
- File created: `test/ui/TestScreenScreen.kt`
- Composable function compiles without errors

### Test 3: Generate Full Feature

1. Right-click on package: `com.yourapp.features.sample`
2. Select **New → FastCodeGen → Full Feature**
3. Configure:
   ```
   Feature Name: Sample
   ✅ Generate Screen
   ✅ Generate ViewModel
   ✅ Generate Repository
   
   Screen: Simple navigation with back
   ViewModel: All features enabled
   Repository: 1 method with HttpClient
   ```
4. Click **Generate**

**Expected Result:**
- Complete feature structure created
- All files compile without errors
- Proper package structure

---

## Verification

### Checklist

After setup, verify the following:

- [ ] Base classes compile without errors
- [ ] Navigation utilities are accessible
- [ ] Plugin settings are saved correctly
- [ ] Test ViewModel generation successful
- [ ] Test Screen generation successful
- [ ] Test Repository generation successful
- [ ] Generated code has correct imports
- [ ] Generated code follows your project structure
- [ ] Dependency injection is working

### Common Issues and Solutions

#### Issue: Import Errors in Generated Code

**Problem:** Generated code shows unresolved imports

**Solution:**
1. Open Settings (⚙️)
2. Verify each path is correct
3. Re-browse paths if needed
4. Ensure base classes are in correct packages

#### Issue: Navigation Utilities Not Found

**Problem:** Screen generation fails with navigation errors

**Solution:**
1. Create `NavigationUtils.kt` in `core/utilities`
2. Add `composableRoute` and `composableSafeType` functions
3. Re-configure navigation paths in settings

#### Issue: ViewModel Doesn't Compile

**Problem:** Generated ViewModel has compilation errors

**Solution:**
1. Check AppViewModel implementation
2. Ensure all abstract methods are implemented
3. Verify generics are correctly defined

---

## Next Steps

Now that your setup is complete:

1. **Read the User Guide** - Learn all features with examples
2. **Generate Real Features** - Start with a simple feature
3. **Customize Base Classes** - Adapt to your specific needs
4. **Share with Team** - Help others set up their environment
5. **Report Issues** - Contribute feedback for improvements

---

## Additional Resources

- 📖 [README](README.md) - Overview and features
- 📚 [User Guide](USER_GUIDE.md) - Detailed tutorials
- ⚡ [Quick Reference](QUICK_REFERENCE.md) - Cheat sheet
- 📊 [Plugin Summary](PLUGIN_SUMMARY.md) - Feature summary

---

## Support

Need help with setup?
- 📧 Email: alialfayed.official@gmail.com
- 💼 LinkedIn: [alfayedoficial](https://www.linkedin.com/in/alfayedoficial/)
- 🐛 GitHub: [Report Issues](https://github.com/alfayedoficial/FastCodeGen/issues)

---

**Setup Complete! 🎉**

You're now ready to use FastCodeGen to accelerate your Kotlin development!