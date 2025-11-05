# FastCodeGen User Guide

**Version 1.0.6 - Complete Tutorial and Examples**

---

## Table of Contents

1. [Getting Started](#getting-started)
2. [Initial Setup](#initial-setup)
3. [Screen Generation](#screen-generation)
4. [ViewModel Generation](#viewmodel-generation)
5. [Repository Generation](#repository-generation)
6. [Full Feature Generation](#full-feature-generation)
7. [Advanced Examples](#advanced-examples)
8. [Best Practices](#best-practices)
9. [Troubleshooting](#troubleshooting)

---

## Getting Started

### Prerequisites

Before using FastCodeGen, ensure you have:
- IntelliJ IDEA 2024.2+ or Android Studio Koala+
- Kotlin plugin enabled
- A Kotlin project with standard source structure
- Base ViewModel, State, and Intent classes in your project
- (Optional) Jetpack Compose for screen generation

### Installation

1. Open **Settings → Plugins → Marketplace**
2. Search for **"FastCodeGen"**
3. Click **Install** and restart IDE

---

## Initial Setup

### Step 1: Create Base Classes

First, ensure you have the required base classes in your project. Here's a minimal example structure:

```kotlin
// AppViewModel.kt
abstract class AppViewModel<STATE : BaseState, EVENT : BaseEvent, UISTATE : BaseUIState, INTENT : BaseIntent>(
    initialState: STATE,
    initialUIState: UISTATE,
    config: ViewModelConfig
) : ViewModel() {
    abstract fun handleIntent(intent: INTENT)
    abstract fun createErrorState(message: String): STATE
    abstract fun createErrorEvent(message: String): EVENT
    
    protected fun launch(block: suspend () -> Unit) { /* ... */ }
    protected fun setState(state: STATE) { /* ... */ }
    protected fun refreshRequest(block: () -> Unit) { /* ... */ }
}

// ViewModelConfig.kt
data class ViewModelConfig(
    val enableRefresh: Boolean = false,
    val enableEvents: Boolean = false
)

// Base interfaces
interface BaseState
interface BaseEvent
interface BaseUIState
interface BaseIntent
interface Refreshable {
    fun withRefresh(isRefresh: Boolean): BaseUIState
}

// NoEvent for when events are disabled
object NoEvent : BaseEvent
```

### Step 2: Configure Plugin Settings

1. Right-click on any package
2. Select **New → FastCodeGen**
3. Click the **⚙️ Settings** button
4. Configure each path:
   - Click **📁** next to each field
   - Browse to your base class file
   - Plugin auto-detects the package path
   - Repeat for all required fields

**Required Settings:**
```
AppViewModel: com.myapp.core.viewmodel.AppViewModel
ViewModelConfig: com.myapp.core.viewmodel.ViewModelConfig
BaseState: com.myapp.core.viewmodel.BaseState
BaseEvent: com.myapp.core.viewmodel.BaseEvent
BaseUIState: com.myapp.core.viewmodel.BaseUIState
Refreshable: com.myapp.core.viewmodel.Refreshable
BaseIntent: com.myapp.core.viewmodel.BaseIntent
composableRoute: com.myapp.core.utilities.composableRoute
composableSafeType: com.myapp.core.utilities.composableSafeType
```

5. Click **OK** to save

---

## Screen Generation

### Basic Screen Without Navigation

**Use Case:** Simple screen with no navigation parameters

**Steps:**
1. Right-click on your feature package
2. Select **New → FastCodeGen**
3. Select **Screen**
4. Click **Next →**
5. Configure:
   ```
   Feature Name: About
   ☐ Has Navigation Back
   Navigation Type: None
   ```
6. Click **Generate**

**Generated:**
```kotlin
package com.myapp.about.ui

import androidx.compose.runtime.*

@Composable
fun AboutScreen() {
    // TODO: Implement your screen content
}
```

### Screen with Simple Navigation

**Use Case:** Screen with basic navigation using route constants

**Steps:**
1. Right-click on feature package
2. Select **New → FastCodeGen → Screen**
3. Configure:
   ```
   Feature Name: Settings
   ✅ Has Navigation Back
   Navigation Type: Simple
   ```
4. Click **Generate**

**Generated:**
```kotlin
package com.myapp.settings.ui

import androidx.compose.runtime.*
import com.myapp.core.utilities.composableRoute

@Composable
fun SettingsScreen(
    navigationBack: () -> Unit
) {
    composableRoute(
        navigationBack = navigationBack
    ) {
        // TODO: Implement your screen content
    }
}
```

### Screen with Type-Safe Navigation

**Use Case:** Screen with strongly-typed navigation parameters

**Steps:**
1. Right-click on feature package
2. Select **New → FastCodeGen → Screen**
3. Configure:
   ```
   Feature Name: ProductDetail
   ✅ Has Navigation Back
   Navigation Type: Type-Safe
   Parameters:
     - productId: String
     - showReviews: Boolean
   ```
4. Click **Generate**

**Generated:**
```kotlin
package com.myapp.productdetail.ui

import androidx.compose.runtime.*
import com.myapp.core.utilities.composableSafeType

@Composable
fun ProductDetailScreen(
    productId: String,
    showReviews: Boolean,
    navigationBack: () -> Unit
) {
    composableSafeType(
        navigationBack = navigationBack
    ) {
        // TODO: Implement your screen content
        // Access: productId, showReviews
    }
}
```

### Screen with ViewModel Integration

**Use Case:** Screen connected to ViewModel for state management

**Steps:**
1. Generate ViewModel first (or use Full Feature)
2. Right-click on feature package
3. Select **New → FastCodeGen → Screen**
4. Configure:
   ```
   Feature Name: Profile
   ✅ Has Navigation Back
   Navigation Type: Type-Safe
   Parameters:
     - userId: String
   ```
5. Click **Generate**

**Generated:**
```kotlin
package com.myapp.profile.ui

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myapp.profile.viewmodel.ProfileIntent
import com.myapp.profile.viewmodel.ProfileViewModel
import com.myapp.profile.viewmodel.state.ProfileState
import com.myapp.core.utilities.composableSafeType
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    userId: String,
    navigationBack: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProfileIntent.LoadProfile)
    }

    composableSafeType(
        navigationBack = navigationBack
    ) {
        when (val currentState = state) {
            is ProfileState.Loading -> LoadingContent()
            is ProfileState.Success -> ProfileContent(
                data = currentState.data,
                uiState = uiState,
                onIntent = viewModel::handleIntent
            )
            is ProfileState.Error -> ErrorContent(
                message = currentState.message
            )
            ProfileState.Idle -> IdleContent()
        }
    }
}
```

---

## ViewModel Generation

### Basic ViewModel

**Use Case:** Simple ViewModel without extras

**Steps:**
1. Right-click on feature package
2. Select **New → FastCodeGen → ViewModel State**
3. Configure:
   ```
   Feature Name: Dashboard
   ☐ Enable Events
   ☐ Enable Refresh
   ☐ Enable UIState
   ☐ Include Load Method
   Use Cases: (leave empty)
   ```
4. Click **Generate**

**Generated Files:**
```
dashboard/
└── viewmodel/
    ├── state/
    │   └── DashboardState.kt
    └── DashboardViewModel.kt
```

**DashboardState.kt:**
```kotlin
package com.myapp.dashboard.viewmodel.state

import com.myapp.core.viewmodel.BaseState
import com.myapp.core.viewmodel.BaseIntent

// State
sealed class DashboardState : BaseState {
    data object Idle : DashboardState()
    data class Error(val message: String) : DashboardState()
}

// Intent
sealed class DashboardIntent : BaseIntent {
    data object ClearState : DashboardIntent()
}
```

**DashboardViewModel.kt:**
```kotlin
package com.myapp.dashboard.viewmodel

import com.myapp.core.viewmodel.AppViewModel
import com.myapp.core.viewmodel.ViewModelConfig
import com.myapp.core.viewmodel.NoEvent
import com.myapp.dashboard.viewmodel.state.DashboardIntent
import com.myapp.dashboard.viewmodel.state.DashboardState

class DashboardViewModel() : AppViewModel<DashboardState, NoEvent, Unit, DashboardIntent>(
    initialState = DashboardState.Idle,
    initialUIState = Unit,
    config = ViewModelConfig(
        enableRefresh = false,
        enableEvents = false
    )
) {

    override fun handleIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.ClearState -> setState(DashboardState.Idle)
        }
    }

    override fun createErrorState(message: String): DashboardState {
        return DashboardState.Error(message)
    }
}
```

### ViewModel with All Features

**Use Case:** Full-featured ViewModel with events, refresh, UIState, and load method

**Steps:**
1. Right-click on feature package
2. Select **New → FastCodeGen → ViewModel State**
3. Configure:
   ```
   Feature Name: UserProfile
   ✅ Enable Events
   ✅ Enable Refresh
   ✅ Enable UIState
   ✅ Include Load Method
   Use Cases: GetUserUseCase, UpdateUserUseCase
   ```
4. Click **Generate**

**Generated UserProfileState.kt:**
```kotlin
package com.myapp.userprofile.viewmodel.state

import com.myapp.core.viewmodel.*

// State
sealed class UserProfileState : BaseState {
    data object Idle : UserProfileState()
    data object Loading : UserProfileState()
    data object Success : UserProfileState()
    data class Error(val message: String) : UserProfileState()
}

// Event
sealed class UserProfileEvent : BaseEvent {
    data object Loading : UserProfileEvent()
    data object Success : UserProfileEvent()
    data class Error(val message: String) : UserProfileEvent()
}

// UIState
data class UserProfileUIState(
    val isRefresh: Boolean = false,
    val isLoading: Boolean = false,
    // TODO: Add your UI state properties here
) : BaseUIState, Refreshable {
    override fun withRefresh(isRefresh: Boolean): BaseUIState {
        return copy(isRefresh = isRefresh)
    }
}

// Intent
sealed class UserProfileIntent : BaseIntent {
    data object ClearState : UserProfileIntent()
    data object LoadUserProfile : UserProfileIntent()
    data object RefreshRequest : UserProfileIntent()
    // TODO: Add your custom intents here
}
```

**Generated UserProfileViewModel.kt:**
```kotlin
package com.myapp.userprofile.viewmodel

import com.myapp.core.viewmodel.AppViewModel
import com.myapp.core.viewmodel.ViewModelConfig
import com.myapp.userprofile.viewmodel.state.*

class UserProfileViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : AppViewModel<UserProfileState, UserProfileEvent, UserProfileUIState, UserProfileIntent>(
    initialState = UserProfileState.Idle,
    initialUIState = UserProfileUIState(),
    config = ViewModelConfig(
        enableRefresh = true,
        enableEvents = true
    )
) {

    init {
        loadUserProfile()
    }

    override fun handleIntent(intent: UserProfileIntent) {
        when (intent) {
            is UserProfileIntent.ClearState -> setState(UserProfileState.Idle)
            is UserProfileIntent.LoadUserProfile -> loadUserProfile()
            is UserProfileIntent.RefreshRequest -> refreshRequest { loadUserProfile() }
        }
    }

    override fun createErrorState(message: String): UserProfileState {
        return UserProfileState.Error(message)
    }

    override fun createErrorEvent(message: String): UserProfileEvent {
        return UserProfileEvent.Error(message)
    }

    private fun loadUserProfile() {
        launch {
            setState(UserProfileState.Loading)
            // TODO: Implement
        }
    }
}
```

---

## Repository Generation

### Basic Repository

**Use Case:** Simple repository without methods

**Steps:**
1. Right-click on feature package
2. Select **New → FastCodeGen → Repository**
3. Configure:
   ```
   Feature Name: Analytics
   ✅ Needs HttpClient
   Methods: (leave empty or remove default method)
   ```
4. Click **Generate**

**Generated Files:**
```
analytics/
├── domain/
│   └── repo/
│       └── AnalyticsRepo.kt
└── data/
    └── repo/
        └── AnalyticsRepoImpl.kt
```

### Repository with Custom Methods

**Use Case:** Repository with specific API endpoints

**Steps:**
1. Right-click on feature package
2. Select **New → FastCodeGen → Repository**
3. Configure:
   ```
   Feature Name: Product
   ✅ Needs HttpClient
   Methods:
     Method 1:
       Name: getProducts
       Return Type: Flow<List<Product>>
       Parameters: categoryId: String, page: Int
     
     Method 2:
       Name: getProductDetail
       Return Type: Flow<Product>
       Parameters: productId: String
     
     Method 3:
       Name: searchProducts
       Return Type: Flow<List<Product>>
       Parameters: query: String, filters: Map<String, String>
   ```
4. Click **Generate**

**Generated ProductRepo.kt:**
```kotlin
package com.myapp.product.domain.repo

import kotlinx.coroutines.flow.Flow

interface ProductRepo {

    fun getProducts(categoryId: String, page: Int): Flow<List<Product>>

    fun getProductDetail(productId: String): Flow<Product>

    fun searchProducts(query: String, filters: Map<String, String>): Flow<List<Product>>

}
```

**Generated ProductRepoImpl.kt:**
```kotlin
package com.myapp.product.data.repo

import com.myapp.product.domain.repo.ProductRepo
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

class ProductRepoImpl(
    private val httpClient: HttpClient,
) : ProductRepo {

    override fun getProducts(categoryId: String, page: Int): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun getProductDetail(productId: String): Flow<Product> {
        TODO("Not yet implemented")
    }

    override fun searchProducts(query: String, filters: Map<String, String>): Flow<List<Product>> {
        TODO("Not yet implemented")
    }
}
```

### Repository Without HttpClient

**Use Case:** Local database repository

**Steps:**
1. Right-click on feature package
2. Select **New → FastCodeGen → Repository**
3. Configure:
   ```
   Feature Name: Cache
   ☐ Needs HttpClient
   Methods:
     - saveData(key: String, value: String) → Flow<Unit>
     - getData(key: String) → Flow<String>
     - clearCache() → Flow<Unit>
   ```
4. Click **Generate**

---

## Full Feature Generation

### Complete Feature with Everything

**Use Case:** Generate entire feature with Screen, ViewModel, and Repository

**Steps:**
1. Right-click on feature package
2. Select **New → FastCodeGen → Full Feature**
3. Configure:
   ```
   Feature Name: Checkout
   
   Generation Options:
   ✅ Generate Screen
   ✅ Generate ViewModel
   ✅ Generate Repository
   
   Screen Configuration:
   ✅ Has Navigation Back
   Navigation Type: Type-Safe
   Parameters:
     - cartId: String
     - promoCode: String
   
   ViewModel Configuration:
   ✅ Enable Events
   ✅ Enable Refresh
   ✅ Enable UIState
   ✅ Include Load Method
   Use Cases: CalculateTotalUseCase, ProcessPaymentUseCase
   
   Repository Configuration:
   ✅ Needs HttpClient
   Methods:
     - calculateTotal(cartId: String, promoCode: String) → Flow<CheckoutTotal>
     - processPayment(paymentInfo: PaymentInfo) → Flow<PaymentResult>
     - verifyPromoCode(code: String) → Flow<PromoCodeValidation>
   ```
4. Click **Generate**

**Generated Structure:**
```
checkout/
├── ui/
│   └── CheckoutScreen.kt
├── viewmodel/
│   ├── state/
│   │   ├── CheckoutState.kt
│   │   ├── CheckoutEvent.kt
│   │   ├── CheckoutUIState.kt
│   │   └── CheckoutIntent.kt
│   └── CheckoutViewModel.kt
├── domain/
│   └── repo/
│       └── CheckoutRepo.kt
└── data/
    └── repo/
        └── CheckoutRepoImpl.kt
```

### Feature with Only Screen and ViewModel

**Use Case:** UI-heavy feature without repository

**Steps:**
1. Configure:
   ```
   Feature Name: Onboarding
   
   ✅ Generate Screen
   ✅ Generate ViewModel
   ☐ Generate Repository
   
   Screen: Type-Safe with no parameters
   ViewModel: All features enabled
   ```

### Feature with Only ViewModel and Repository

**Use Case:** Background service or business logic

**Steps:**
1. Configure:
   ```
   Feature Name: Synchronization
   
   ☐ Generate Screen
   ✅ Generate ViewModel
   ✅ Generate Repository
   ```

---

## Advanced Examples

### Example 1: E-Commerce Product Listing

**Scenario:** Create a product listing feature with pagination, filters, and search

```
Feature Name: ProductListing

Screen Configuration:
✅ Has Navigation Back
Navigation Type: Type-Safe
Parameters:
  - categoryId: String
  - initialFilter: String

ViewModel Configuration:
✅ Enable Events (for navigation to detail)
✅ Enable Refresh (for pull-to-refresh)
✅ Enable UIState (for filters, search query, pagination)
✅ Include Load Method
Use Cases: GetProductsUseCase, SearchProductsUseCase

Repository Configuration:
✅ Needs HttpClient
Methods:
  - getProducts(categoryId: String, page: Int, filters: Map<String, String>) → Flow<ProductPage>
  - searchProducts(query: String, categoryId: String) → Flow<List<Product>>
  - getFilters(categoryId: String) → Flow<List<Filter>>
```

### Example 2: User Authentication Flow

**Scenario:** Login screen with validation and error handling

```
Feature Name: Login

Screen Configuration:
☐ Has Navigation Back (it's the entry point)
Navigation Type: Simple

ViewModel Configuration:
✅ Enable Events (for navigation after success)
☐ Enable Refresh
✅ Enable UIState (for email, password, loading states)
✅ Include Load Method (to check existing session)
Use Cases: AuthenticateUseCase, ValidateCredentialsUseCase

Repository Configuration:
✅ Needs HttpClient
Methods:
  - login(email: String, password: String) → Flow<AuthResult>
  - validateSession(token: String) → Flow<Boolean>
  - logout() → Flow<Unit>
```

### Example 3: Settings Screen

**Scenario:** Simple settings screen without repository

```
Feature Name: Settings

Screen Configuration:
✅ Has Navigation Back
Navigation Type: Simple

ViewModel Configuration:
☐ Enable Events
☐ Enable Refresh
✅ Enable UIState (for settings values)
☐ Include Load Method
Use Cases: (none - local preferences)

Repository: Not generated
```

---

## Best Practices

### 1. Naming Conventions

**Feature Names:**
- Use PascalCase for multi-word features: `UserProfile`, `ProductDetail`
- Use single words when appropriate: `Login`, `Dashboard`
- Be descriptive: `CheckoutPayment` instead of just `Payment`

**Use Cases:**
- Always use PascalCase
- End with `UseCase`: `GetUserDataUseCase`, `UpdateProfileUseCase`

**Repository Methods:**
- Use camelCase
- Start with verb: `getUser`, `updateProfile`, `deleteAccount`
- Be specific: `getActiveProducts` instead of `getProducts`

### 2. When to Enable Features

**Enable Events When:**
- You need one-time UI actions (navigation, toasts, dialogs)
- You want to separate concerns between state and side effects
- You have temporary UI notifications

**Enable Refresh When:**
- Your screen supports pull-to-refresh
- You have periodic data updates
- Users need to manually reload data

**Enable UIState When:**
- You have form fields or input states
- You need local UI-only state (like expanded/collapsed)
- You want to separate business state from UI state

**Include Load Method When:**
- ViewModel needs initialization on creation
- You want to load initial data automatically
- You have setup logic that runs once

### 3. Repository Method Design

**Good Method Design:**
```kotlin
// ✅ Clear, specific, type-safe
fun getUser(userId: String): Flow<User>
fun updateProfile(userId: String, profile: ProfileUpdate): Flow<Unit>
fun searchProducts(query: String, filters: ProductFilters): Flow<List<Product>>
```

**Avoid:**
```kotlin
// ❌ Too generic, unclear parameters
fun getData(id: String): Flow<Any>
fun update(map: Map<String, Any>): Flow<Unit>
fun search(vararg params: String): Flow<List<Any>>
```

### 4. Project Structure

**Recommended Package Structure:**
```
com.myapp/
├── core/
│   ├── viewmodel/           # Base classes
│   └── utilities/           # Navigation utilities
├── features/
│   ├── login/
│   │   ├── ui/
│   │   ├── viewmodel/
│   │   ├── domain/repo/
│   │   └── data/repo/
│   ├── dashboard/
│   └── settings/
└── di/                      # Dependency injection
```

### 5. Settings Configuration Tips

1. **Always use browse button (📁)** - Ensures correct path detection
2. **Verify paths after browsing** - Check that package path is complete
3. **Keep settings consistent** - Use same package structure for all base classes
4. **Document your structure** - Help team members configure correctly

---

## Troubleshooting

### Issue: "Settings not configured" error

**Problem:** Plugin shows error when trying to generate code

**Solutions:**
1. Click ⚙️ Settings button in the dialog
2. Configure ALL required paths (use 📁 browse button)
3. Click OK to save
4. Try generating again

### Issue: Generated code doesn't compile

**Problem:** Import errors or unresolved references

**Solutions:**
1. **Check settings paths:**
   - Open Settings (⚙️)
   - Verify each path matches your actual base classes
   - Use 📁 to re-detect if needed

2. **Verify base classes exist:**
   - Navigate to each configured path in your project
   - Ensure classes/interfaces are properly defined

3. **Check Kotlin version:**
   - Ensure you're using Kotlin 1.9.0 or later
   - Update Kotlin plugin if needed

### Issue: Screen generation doesn't work

**Problem:** Screen file not created or has errors

**Solutions:**
1. **Check Compose dependencies:**
   ```gradle
   implementation("androidx.compose.ui:ui:1.5.0")
   implementation("androidx.navigation:navigation-compose:2.7.0")
   ```

2. **Verify navigation utility paths:**
   - Open Settings
   - Check `composableRoute` path
   - Check `composableSafeType` path
   - Ensure these functions exist in your project

3. **Check navigation type:**
   - For Simple: ensure `composableRoute` is configured
   - For Type-Safe: ensure `composableSafeType` is configured

### Issue: ViewModel with use cases has compilation errors

**Problem:** Use case dependencies not resolved

**Solutions:**
1. **Check use case names:**
   - Ensure they match your actual use case classes
   - Use correct casing: `GetUserUseCase`, not `getUserUseCase`

2. **Add use cases to DI:**
   ```kotlin
   single { GetUserUseCase(get()) }
   single { UpdateUserUseCase(get()) }
   ```

3. **Import use cases:**
   - Manually add missing imports if needed

### Issue: Repository methods have wrong signatures

**Problem:** Method parameters or return types are incorrect

**Solutions:**
1. **Re-generate with correct configuration:**
   - Use exact type names: `Flow<User>`, not `Flow<user>`
   - Include all parameters with types

2. **Follow proper syntax:**
   ```
   ✅ Correct:
   Parameters: userId: String, includeProfile: Boolean
   Return Type: Flow<UserDetail>
   
   ❌ Incorrect:
   Parameters: userId, includeProfile
   Return Type: UserDetail
   ```

### Issue: Can't find the plugin menu

**Problem:** "New → FastCodeGen" doesn't appear

**Solutions:**
1. **Check context:**
   - Right-click on a package/folder, not a file
   - Must be in a Kotlin source folder

2. **Verify installation:**
   - Settings → Plugins → Installed
   - Search for "FastCodeGen"
   - Restart IDE if needed

3. **Check IDE version:**
   - Requires IntelliJ 2024.2+ or Android Studio Koala+
   - Update if necessary

---

## Next Steps

Now that you've learned how to use FastCodeGen:

1. **Start with simple features** - Generate a basic ViewModel to get familiar
2. **Try screen generation** - Create a Compose screen with navigation
3. **Build complete features** - Use Full Feature generation for new features
4. **Customize for your project** - Adapt settings to your architecture
5. **Share with your team** - Help others configure and use the plugin

For more information:
- 📖 [README](README.md) - Getting started guide
- ⚡ [Quick Reference](QUICK_REFERENCE.md) - Cheat sheet
- 🔧 [Implementation Steps](IMPLEMENTATION_STEPS.md) - Setup guide

---

**Happy Coding! 🚀**