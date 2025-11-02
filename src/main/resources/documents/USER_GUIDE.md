# FastCodeGen - User Guide

**Version:** 1.0.6  
**Last Updated:** November 2, 2025

## Table of Contents

1. [Getting Started](#getting-started)
2. [Settings Configuration](#settings-configuration)
3. [Generating Code](#generating-code)
4. [Advanced Features](#advanced-features)
5. [Best Practices](#best-practices)
6. [Troubleshooting](#troubleshooting)

---

## Getting Started

### Installation

1. Open your IDE (IntelliJ IDEA or Android Studio)
2. Go to **Settings/Preferences** → **Plugins**
3. Search for **"FastCodeGen"**
4. Click **Install**
5. Restart your IDE

### First Launch

When you first use FastCodeGen, you'll see a welcome dialog. This dialog introduces you to the key features and directs you to this user guide.

---

## Settings Configuration

### Why Settings Matter

FastCodeGen needs to know the paths to your base classes to generate code that compiles correctly in your project. This is what makes version 1.0.6 so powerful - it adapts to **your** project structure!

### Opening Settings

**Method 1: From Dialog**
1. Right-click on a package
2. Select **New → FastCodeGen**
3. Click the **⚙️** button (top-right corner)

**Method 2: From Menu**
1. Go to **Tools** → **FastCodeGen Documentation**
2. Select **Settings**

### Configuring Base Classes

For each required field:

1. **Click the 📁 browse button**
2. Navigate to your base class file
3. Select the file (e.g., `AppViewModel.kt`)
4. The plugin automatically detects the full package path
5. Repeat for all required fields

#### Required Settings

| Setting | Example Path | Purpose |
|---------|-------------|---------|
| **AppViewModel** | `com.myapp.core.viewmodel.AppViewModel` | Base ViewModel class |
| **ViewModelConfig** | `com.myapp.core.viewmodel.ViewModelConfig` | ViewModel configuration |
| **BaseState** | `com.myapp.core.viewmodel.BaseState` | State interface |
| **BaseEvent** | `com.myapp.core.viewmodel.BaseEvent` | Event interface |
| **BaseUIState** | `com.myapp.core.viewmodel.BaseUIState` | UI state interface |
| **Refreshable** | `com.myapp.core.viewmodel.Refreshable` | Refresh interface |
| **BaseIntent** | `com.myapp.core.viewmodel.BaseIntent` | Intent interface |

#### Optional Settings

| Setting | Example Path | Purpose |
|---------|-------------|---------|
| **Koin Module** | `org.koin.core.module.Module` | Dependency injection (leave empty if not using) |

### Manual Entry

If you prefer, you can manually type the full package path:
```
com.yourpackage.core.viewmodel.AppViewModel
```

### Validation

Click **OK** to save. FastCodeGen will validate that:
- All required fields are filled
- Paths follow proper format
- No fields are blank

---

## Generating Code

### Step-by-Step Guide

#### 1. Start Generation

**Right-click** on any package or folder where you want to generate code:
```
src/main/kotlin/com/yourapp/features/
```

Select **New → FastCodeGen**

#### 2. Choose Generation Type

You'll see three options:

##### 🎯 ViewModel State
- Generates ViewModel and State files
- Perfect for UI-only features
- **Use when:** You already have data layer

##### 📦 Repository
- Generates Repository interface and implementation
- Perfect for data layer only
- **Use when:** You already have ViewModel

##### 🚀 Full Feature
- Generates both ViewModel and Repository
- Complete feature in one go
- **Use when:** Starting fresh feature

Click **Next →**

#### 3. Configure Your Code

**Feature Name:**
```
Login
```
This becomes: `LoginViewModel`, `LoginState`, `LoginRepo`, etc.

**Configuration Options:**

| Option | Description | When to Use |
|--------|-------------|-------------|
| **Enable Events** | Adds event handling | One-time UI actions (toast, navigation) |
| **Enable Refresh** | Adds pull-to-refresh | Lists, data that needs refreshing |
| **Enable UIState** | Adds UI state data class | Complex UI state with multiple properties |
| **Use Cases** | Specify dependencies | When ViewModel needs use cases |
| **HTTP Client** | Includes Ktor client | Repository needs network calls |

**Repository Methods (Optional):**

Add as many methods as needed:
```
Method Name: getUser
Return Type: User
Parameters: userId: String

Method Name: updateProfile
Return Type: Unit
Parameters: profile: UserProfile
```

💡 **Tip:** You can leave return type or parameters empty!

#### 4. Generate

Click **Generate** and watch the magic happen! ✨

---

## Advanced Features

### Optional Repository Methods

**New in 1.0.6:** You can now:
- Create repositories without any methods
- Have methods without parameters
- Have methods without return types (defaults to `Unit`)

**Examples:**

```kotlin
// No parameters
fun refreshData(): Flow<Unit>

// No return type (Unit)
fun clearCache()

// Both parameters and return type
fun fetchUser(id: String): Flow<User>
```

### Empty Feature

Want just the structure? Generate with:
- Feature name only
- No methods
- Minimal configuration

Perfect for setting up the skeleton!

### Multiple Use Cases

Separate use cases with commas:
```
Authentication, Validation, Analytics
```

Generates:
```kotlin
class LoginViewModel(
    private val authenticationUseCase: AuthenticationUseCase,
    private val validationUseCase: ValidationUseCase,
    private val analyticsUseCase: AnalyticsUseCase
) : AppViewModel<...>
```

### Customizing Generated Code

After generation, you can:
1. Add more states to the sealed class
2. Add more events
3. Implement TODO functions
4. Add custom intents
5. Extend UIState with more properties

---

## Best Practices

### 1. Consistent Naming

Always use clear, descriptive names:
✅ **Good:** `Login`, `UserProfile`, `Settings`  
❌ **Bad:** `Screen1`, `Feature`, `Test`

### 2. Feature Organization

Organize by feature, not by layer:
```
✅ GOOD
features/
├── login/
│   ├── viewmodel/
│   └── data/
└── profile/
    ├── viewmodel/
    └── data/

❌ BAD
viewmodels/
├── LoginViewModel
└── ProfileViewModel
data/
├── LoginRepo
└── ProfileRepo
```

### 3. Use Cases

Only add use cases that the ViewModel actually needs:
```
✅ Login → Authentication, Validation
✅ UserList → FetchUsers
❌ Login → All possible use cases
```

### 4. Repository Methods

Keep methods focused and specific:
```
✅ getUser(id: String)
✅ updateUserProfile(profile: Profile)
❌ doEverything()
```

### 5. Settings Management

- **Configure once** per project
- **Document** your base class locations
- **Update** settings when base classes change
- **Share** settings approach with team

---

## Troubleshooting

### Issue: Settings Dialog Shows Errors

**Symptom:** Red text showing missing fields

**Solution:**
1. Click 📁 for each red field
2. Browse to the actual file
3. Verify file exists in your project
4. Click OK

---

### Issue: Generated Code Won't Compile

**Symptom:** Import errors or red underlines

**Solutions:**

1. **Check Settings:**
   - Open settings (⚙️)
   - Verify all paths are correct
   - Click 📁 to auto-detect again

2. **Verify Base Classes:**
   - Ensure base classes exist
   - Check spelling and package names
   - Make sure they're in the correct module

3. **Clean & Rebuild:**
   ```
   Build → Clean Project
   Build → Rebuild Project
   ```

4. **Invalidate Caches:**
   ```
   File → Invalidate Caches → Invalidate and Restart
   ```

---

### Issue: Plugin Menu Not Appearing

**Symptom:** "FastCodeGen" not in New menu

**Solutions:**

1. **Check IDE Version:**
   - Must be 2024.2 or later
   - Update if older

2. **Enable Kotlin Plugin:**
   ```
   Settings → Plugins → Search "Kotlin"
   Enable if disabled
   ```

3. **Right Location:**
   - Right-click on a **package** or **folder**
   - Must be in `src/main/kotlin/` or similar

---

### Issue: Browse Button Doesn't Work

**Symptom:** File chooser doesn't open

**Solution:**
- Check file permissions
- Verify project is properly opened
- Try manual entry instead

---

### Issue: Wrong Imports Generated

**Symptom:** Generated files have wrong package paths

**Solution:**
1. Open Settings
2. Click 📁 for each field
3. Select the correct file
4. Verify auto-detected path
5. Save and try again

---

## Tips & Tricks

### Tip 1: Quick Settings Access
Press **Ctrl/Cmd + Shift + A** → Type "FastCodeGen" → Select Settings

### Tip 2: Documentation Access
Click **📚 Help** button in the dialog for instant access to all docs

### Tip 3: Keyboard Shortcuts
- **Tab** - Navigate between fields
- **Enter** - Click OK/Generate
- **Esc** - Cancel dialog

### Tip 4: Batch Generation
Generate multiple features quickly:
1. Generate Feature A
2. Press **Ctrl/Cmd + Z** to stay in same location
3. Right-click → New → FastCodeGen
4. Generate Feature B

### Tip 5: Copy Settings
Settings are project-based. To copy to another project:
1. Note down all package paths
2. Open new project
3. Configure settings with same paths
4. Or keep base classes in shared module

---

## Examples

### Example 1: Simple ViewModel

**Goal:** Create login ViewModel with events

**Steps:**
1. Right-click on `com.app.features`
2. New → FastCodeGen → ViewModel State
3. Name: `Login`
4. ✓ Enable Events
5. Generate

**Result:**
```kotlin
LoginState.kt - State management
LoginViewModel.kt - ViewModel with events
```

---

### Example 2: Repository with Methods

**Goal:** Create user repository

**Steps:**
1. Right-click on `com.app.features`
2. New → FastCodeGen → Repository
3. Name: `User`
4. ✓ Needs HttpClient
5. Add methods:
   - `getUser(id: String)` → `User`
   - `updateUser(user: User)` → `Unit`
6. Generate

**Result:**
```kotlin
domain/repo/UserRepo.kt - Interface
data/repo/UserRepoImpl.kt - Implementation with HttpClient
```

---

### Example 3: Complete Feature

**Goal:** Build profile feature with data and UI

**Steps:**
1. Right-click on `com.app.features`
2. New → FastCodeGen → Full Feature
3. Name: `Profile`
4. ViewModel Config:
   - ✓ Enable Events
   - ✓ Enable Refresh
   - ✓ Enable UIState
   - Use Cases: `FetchProfile, UpdateProfile`
5. Repository Config:
   - ✓ Needs HttpClient
   - Methods:
      - `getProfile()` → `Profile`
      - `updateProfile(profile: Profile)` → `Unit`
6. Generate

**Result:** Complete feature ready to implement!

---

## Getting Help

### Documentation
- **README** - Overview and quick start
- **Quick Reference** - Cheat sheet
- **Implementation Steps** - Detailed setup

### Contact
- **Email:** alialfayed.official@gmail.com
- **LinkedIn:** [alfayedoficial](https://www.linkedin.com/in/alfayedoficial/)
- **GitHub:** [alfayedoficial](https://github.com/alfayedoficial)

---

<div align="center">

**Happy Coding! 🚀**

*FastCodeGen v1.0.6 - Making Kotlin development faster*

</div>