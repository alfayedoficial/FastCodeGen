# FastCodeGen - Quick Reference

**Version:** 1.0.6 | Last Updated: November 2, 2025

## 🚀 Quick Start

```
1. Configure Settings (⚙️)
2. Right-click package → New → FastCodeGen
3. Choose type → Configure → Generate
```

---

## ⚙️ Settings (NEW in 1.0.6)

### Access Settings
- Click ⚙️ in FastCodeGen dialog
- Tools → FastCodeGen Documentation → Settings

### Configure Paths
Click 📁 to browse and auto-detect package paths

**Required:**
- AppViewModel
- ViewModelConfig
- BaseState
- BaseEvent
- BaseUIState
- Refreshable
- BaseIntent

**Optional:**
- Koin Module

---

## 🎯 Generation Types

### ViewModel State
```
✓ State, Event, UIState, Intent
✓ ViewModel class
✗ Repository
```

### Repository
```
✗ ViewModel
✓ Repository interface
✓ Repository implementation
```

### Full Feature
```
✓ ViewModel + State
✓ Repository
✓ Complete feature
```

---

## 🔧 Configuration Options

| Option | Purpose | Use When |
|--------|---------|----------|
| **Events** | One-time UI actions | Toast, Navigation |
| **Refresh** | Pull-to-refresh | Lists, Data screens |
| **UIState** | UI state management | Complex UI |
| **Use Cases** | Dependencies | Business logic |
| **HTTP Client** | Network calls | Repository needs API |

---

## 📝 Naming Conventions

**Feature Name Format:**
```
✅ Login          → LoginViewModel
✅ UserProfile    → UserProfileViewModel  
✅ Settings       → SettingsViewModel
❌ login_screen   (use PascalCase)
❌ Screen1        (use descriptive names)
```

---

## 🎨 Generated Structure

### ViewModel State
```
feature/
└── viewmodel/
    ├── state/
    │   └── FeatureState.kt
    └── FeatureViewModel.kt
```

### Repository
```
feature/
├── domain/
│   └── repo/
│       └── FeatureRepo.kt
└── data/
    └── repo/
        └── FeatureRepoImpl.kt
```

### Full Feature
```
feature/
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

---

## 💡 Tips & Tricks

### Settings
```bash
# First time setup
⚙️ → 📁 Browse for each base class → Save

# Verify settings
Generated code should compile immediately
```

### Optional Methods (NEW!)
```kotlin
// All valid:
fun getData(): Flow<Data>              // Normal
fun getData()                          // No return (Unit)
fun getData(id: String): Flow<Data>    // With params
fun refresh()                          // No params, no return
```

### Multiple Use Cases
```
Separate with commas:
Authentication, Validation, Analytics
```

### Batch Generation
```
1. Generate Feature A
2. Stay in same location
3. Right-click → New → FastCodeGen
4. Generate Feature B
```

---

## ⌨️ Keyboard Shortcuts

| Action | Shortcut |
|--------|----------|
| Navigate fields | Tab |
| Confirm | Enter |
| Cancel | Esc |
| Find FastCodeGen | Ctrl/Cmd + Shift + A |

---

## 🐛 Quick Troubleshooting

### Import Errors?
```
1. Check settings (⚙️)
2. Click 📁 to re-detect paths
3. Verify base classes exist
4. Clean & rebuild project
```

### Plugin Not Showing?
```
1. Check IDE version (2024.2+)
2. Enable Kotlin plugin
3. Right-click on package (not file)
4. Invalidate caches if needed
```

### Settings Not Saving?
```
1. Fill all required fields
2. Click OK (not Cancel)
3. Restart IDE if needed
```

---

## 📋 Code Templates

### State
```kotlin
sealed class FeatureState : BaseState {
    data object Idle : FeatureState()
    data object Loading : FeatureState()
    data object Success : FeatureState()
    data class Error(val message: String) : FeatureState()
}
```

### Event
```kotlin
sealed class FeatureEvent : BaseEvent {
    data object Loading : FeatureEvent()
    data object Success : FeatureEvent()
    data class Error(val message: String) : FeatureEvent()
}
```

### UIState
```kotlin
data class FeatureUIState(
    val isRefresh: Boolean = false,
    val isLoading: Boolean = false,
) : BaseUIState, Refreshable {
    override fun withRefresh(isRefresh: Boolean): BaseUIState {
        return copy(isRefresh = isRefresh)
    }
}
```

### Intent
```kotlin
sealed class FeatureIntent : BaseIntent {
    data object ClearState : FeatureIntent()
    data object LoadFeature : FeatureIntent()
    data object RefreshRequest : FeatureIntent()
}
```

### ViewModel
```kotlin
class FeatureViewModel : AppViewModel<
    FeatureState,
    FeatureEvent,
    FeatureUIState,
    FeatureIntent
>(
    initialState = FeatureState.Idle,
    initialUIState = FeatureUIState(),
    config = ViewModelConfig(
        enableRefresh = true,
        enableEvents = true
    )
) {
    override fun handleIntent(intent: FeatureIntent) {
        when (intent) {
            is FeatureIntent.ClearState -> setState(FeatureState.Idle)
            is FeatureIntent.LoadFeature -> loadFeature()
            is FeatureIntent.RefreshRequest -> refreshRequest { loadFeature() }
        }
    }
    
    override fun createErrorState(message: String): FeatureState {
        return FeatureState.Error(message)
    }
    
    override fun createErrorEvent(message: String): FeatureEvent {
        return FeatureEvent.Error(message)
    }
}
```

### Repository Interface
```kotlin
interface FeatureRepo {
    fun getData(id: String): Flow<Data>
    fun updateData(data: Data): Flow<Unit>
}
```

### Repository Implementation
```kotlin
class FeatureRepoImpl(
    private val httpClient: HttpClient
) : FeatureRepo {
    override fun getData(id: String): Flow<Data> {
        TODO("Not yet implemented")
    }
    
    override fun updateData(data: Data): Flow<Unit> {
        TODO("Not yet implemented")
    }
}
```

---

## 🎯 Best Practices

### DO ✅
- Configure settings before first use
- Use descriptive feature names
- Add only needed options
- Keep methods focused
- Generate in correct package

### DON'T ❌
- Skip settings configuration
- Use generic names (Screen1, Test)
- Enable all options unnecessarily
- Create too many methods at once
- Generate in wrong location

---

## 📞 Quick Help

### Documentation
- **📚 Help Button** - In dialog
- **Tools Menu** - FastCodeGen Documentation
- **README** - Full guide
- **User Guide** - Tutorials

### Contact
- Email: alialfayed.official@gmail.com
- LinkedIn: [alfayedoficial](https://www.linkedin.com/in/alfayedoficial/)

---

## 🆕 What's New in 1.0.6

✨ **Settings System** - Configure base class paths  
📁 **Browse Buttons** - Auto-detect package paths  
🎨 **Refactored UI** - Cleaner, more organized  
⚡ **Optional Methods** - Flexible repository generation  
🔧 **Dynamic Imports** - Works with any project  
📚 **Better Docs** - Comprehensive guides

---

<div align="center">

**FastCodeGen v1.0.6**

*Quick. Clean. Kotlin.*

[README](README.md) | [User Guide](USER_GUIDE.md) | [Implementation](IMPLEMENTATION_STEPS.md)

</div>