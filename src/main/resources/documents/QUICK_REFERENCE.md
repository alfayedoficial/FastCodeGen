# FastCodeGen Quick Reference

**Version 1.0.6 - Cheat Sheet**

---

## 🚀 Quick Start

```
1. Settings/Preferences → Plugins → Install "FastCodeGen"
2. Right-click package → New → FastCodeGen → ⚙️ Settings
3. Configure all paths using 📁 browse button
4. Right-click package → New → FastCodeGen → Generate!
```

---

## 📋 Generation Types

| Type | Purpose | Generated Files |
|------|---------|----------------|
| **Screen** | Compose UI | `ui/FeatureScreen.kt` |
| **ViewModel State** | Business logic | `viewmodel/state/*.kt`, `viewmodel/FeatureViewModel.kt` |
| **Repository** | Data layer | `domain/repo/*.kt`, `data/repo/*.kt` |
| **Full Feature** | Complete feature | All of the above |

---

## ⚙️ Settings (Required Paths)

### ViewModel Base Classes
```
AppViewModel:       com.myapp.core.viewmodel.AppViewModel
ViewModelConfig:    com.myapp.core.viewmodel.ViewModelConfig
BaseState:          com.myapp.core.viewmodel.BaseState
BaseEvent:          com.myapp.core.viewmodel.BaseEvent
BaseUIState:        com.myapp.core.viewmodel.BaseUIState
Refreshable:        com.myapp.core.viewmodel.Refreshable
BaseIntent:         com.myapp.core.viewmodel.BaseIntent
```

### Navigation Utilities
```
composableRoute:      com.myapp.core.utilities.composableRoute
composableSafeType:   com.myapp.core.utilities.composableSafeType
```

### Optional
```
Koin Module:        org.koin.core.module.Module
```

---

## 🎨 Screen Generation Options

### Navigation Types

| Type | When to Use | Parameters |
|------|-------------|------------|
| **None** | Simple screen, no navigation | No params |
| **Simple** | Basic navigation with route | No params |
| **Type-Safe** | Type-safe navigation | Yes, define params |

### Navigation Parameters Format
```
Name: userId
Type: String

Name: isEditable
Type: Boolean

Name: itemId
Type: Long
```

### Common Configurations

**No Navigation:**
```
☐ Has Navigation Back
Navigation Type: None
```

**Simple Navigation:**
```
✅ Has Navigation Back
Navigation Type: Simple
```

**Type-Safe with Params:**
```
✅ Has Navigation Back
Navigation Type: Type-Safe
Parameters: userId: String, mode: Int
```

---

## 🎯 ViewModel Configuration

### Checkboxes

| Option | Purpose | When to Enable |
|--------|---------|----------------|
| **Enable Events** | One-time UI actions | Navigation, toasts, dialogs |
| **Enable Refresh** | Pull-to-refresh | User-triggered reload |
| **Enable UIState** | UI-specific state | Form fields, UI flags |
| **Include Load Method** | Auto initialization | Load data on creation |

### Use Cases Format
```
Single: GetUserUseCase
Multiple: GetUserUseCase, UpdateUserUseCase, DeleteUserUseCase
```

### Common Patterns

**Minimal ViewModel:**
```
☐ Enable Events
☐ Enable Refresh
☐ Enable UIState
☐ Include Load Method
Use Cases: (empty)
```

**Full-Featured ViewModel:**
```
✅ Enable Events
✅ Enable Refresh
✅ Enable UIState
✅ Include Load Method
Use Cases: GetDataUseCase, UpdateDataUseCase
```

**Form Screen:**
```
☐ Enable Events
☐ Enable Refresh
✅ Enable UIState (for form fields)
☐ Include Load Method
```

---

## 📦 Repository Configuration

### Method Definition Format

```
Method Name:    getUser
Return Type:    Flow<User>
Parameters:     userId: String

Method Name:    updateProfile
Return Type:    Flow<Unit>
Parameters:     userId: String, profile: ProfileUpdate

Method Name:    searchItems
Return Type:    Flow<List<Item>>
Parameters:     query: String, filters: Map<String, String>
```

### Optional Fields
- **Method Name**: Required (leave others empty to skip method)
- **Return Type**: Optional (defaults to `Unit`)
- **Parameters**: Optional (empty for no params)

### Common Patterns

**CRUD Operations:**
```
1. getItem(id: String) → Flow<Item>
2. createItem(item: Item) → Flow<Item>
3. updateItem(id: String, item: Item) → Flow<Unit>
4. deleteItem(id: String) → Flow<Boolean>
```

**Search/Filter:**
```
1. search(query: String) → Flow<List<Item>>
2. filter(filters: Map<String, String>) → Flow<List<Item>>
3. sort(sortBy: SortType) → Flow<List<Item>>
```

---

## 🏗️ Full Feature Combinations

### Common Scenarios

**Complete Feature:**
```
✅ Generate Screen
✅ Generate ViewModel
✅ Generate Repository
```

**UI Only:**
```
✅ Generate Screen
✅ Generate ViewModel
☐ Generate Repository
```

**Backend Service:**
```
☐ Generate Screen
✅ Generate ViewModel
✅ Generate Repository
```

**Static Screen:**
```
✅ Generate Screen
☐ Generate ViewModel
☐ Generate Repository
```

---

## 📁 Generated File Structure

### Screen Only
```
feature/
└── ui/
    └── FeatureScreen.kt
```

### ViewModel Only
```
feature/
└── viewmodel/
    ├── state/
    │   ├── FeatureState.kt
    │   ├── FeatureEvent.kt      (if enabled)
    │   ├── FeatureUIState.kt    (if enabled)
    │   └── FeatureIntent.kt
    └── FeatureViewModel.kt
```

### Repository Only
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
├── ui/
│   └── FeatureScreen.kt         (if enabled)
├── viewmodel/
│   ├── state/
│   │   └── FeatureState.kt
│   └── FeatureViewModel.kt      (if enabled)
├── domain/
│   └── repo/
│       └── FeatureRepo.kt       (if enabled)
└── data/
    └── repo/
        └── FeatureRepoImpl.kt   (if enabled)
```

---

## 🎯 Common Use Cases

### 1. Login Screen
```
Type: Full Feature
Name: Login

Screen:
  ☐ Navigation Back
  Type: Simple

ViewModel:
  ✅ Events (for navigation)
  ☐ Refresh
  ✅ UIState (for email/password)
  ✅ Load Method
  Use Cases: AuthenticateUseCase

Repository:
  ✅ HttpClient
  - login(email: String, password: String) → Flow<AuthResult>
```

### 2. Profile Detail
```
Type: Full Feature
Name: Profile

Screen:
  ✅ Navigation Back
  Type: Type-Safe
  Params: userId: String

ViewModel:
  ✅ Events
  ✅ Refresh
  ✅ UIState
  ✅ Load Method
  Use Cases: GetProfileUseCase, UpdateProfileUseCase

Repository:
  ✅ HttpClient
  - getProfile(userId: String) → Flow<Profile>
  - updateProfile(userId: String, data: ProfileData) → Flow<Unit>
```

### 3. Settings Screen
```
Type: Screen + ViewModel
Name: Settings

Screen:
  ✅ Navigation Back
  Type: Simple

ViewModel:
  ☐ Events
  ☐ Refresh
  ✅ UIState (for preferences)
  ☐ Load Method
  Use Cases: (none)

Repository: Not generated
```

### 4. Data Sync Service
```
Type: ViewModel + Repository
Name: Sync

ViewModel:
  ☐ Events
  ☐ Refresh
  ☐ UIState
  ✅ Load Method
  Use Cases: SyncDataUseCase

Repository:
  ✅ HttpClient
  - syncData() → Flow<SyncResult>
  - getLastSyncTime() → Flow<Long>

Screen: Not generated
```

### 5. Product List
```
Type: Full Feature
Name: ProductList

Screen:
  ✅ Navigation Back
  Type: Type-Safe
  Params: categoryId: String

ViewModel:
  ✅ Events (for detail navigation)
  ✅ Refresh (pull-to-refresh)
  ✅ UIState (filters, search)
  ✅ Load Method
  Use Cases: GetProductsUseCase, SearchProductsUseCase

Repository:
  ✅ HttpClient
  - getProducts(categoryId: String, page: Int) → Flow<List<Product>>
  - searchProducts(query: String) → Flow<List<Product>>
```

---

## 🔧 Keyboard Shortcuts

| Action | Windows/Linux | macOS |
|--------|---------------|-------|
| Open FastCodeGen | `Alt+Insert` → FastCodeGen | `Cmd+N` → FastCodeGen |
| Open Settings | Click ⚙️ in dialog | Click ⚙️ in dialog |
| Generate | `Enter` or `Alt+G` | `Enter` or `Cmd+G` |
| Cancel | `Esc` | `Esc` |

---

## ⚡ Pro Tips

### Naming Conventions
```
✅ Good:
  - UserProfile
  - ProductDetail
  - CheckoutPayment

❌ Avoid:
  - user_profile
  - productdetail
  - Checkout-Payment
```

### Use Case Naming
```
✅ Good:
  - GetUserDataUseCase
  - UpdateProfileUseCase
  - DeleteAccountUseCase

❌ Avoid:
  - getUserDataUseCase (lowercase)
  - GetUserData (missing UseCase suffix)
  - UserUseCase (not descriptive)
```

### Repository Methods
```
✅ Good:
  - getUser(userId: String): Flow<User>
  - updateProfile(id: String, data: ProfileData): Flow<Unit>

❌ Avoid:
  - GetUser (PascalCase)
  - get_user (snake_case)
  - getData(id: Any): Flow<Any> (too generic)
```

---

## 🐛 Quick Troubleshooting

| Problem | Quick Fix |
|---------|-----------|
| "Settings not configured" | Click ⚙️ → Configure all paths → OK |
| Import errors | Verify settings paths match your classes |
| Screen not generated | Check Compose dependencies |
| Navigation errors | Configure composableRoute/composableSafeType |
| Use case errors | Check naming (PascalCase + UseCase suffix) |
| Plugin menu missing | Right-click on package (not file) |

---

## 📚 Related Documentation

- 📖 [README](README.md) - Complete overview
- 📚 [User Guide](USER_GUIDE.md) - Detailed tutorials
- 🔧 [Implementation Steps](IMPLEMENTATION_STEPS.md) - Setup guide
- 📊 [Plugin Summary](PLUGIN_SUMMARY.md) - Feature overview
- 📑 [Index](INDEX.md) - All documentation

---

## 🔗 Quick Links

- **Settings**: Tools → FastCodeGen Documentation → Settings
- **Help Menu**: 📚 button in FastCodeGen dialog
- **GitHub**: github.com/alfayedoficial/FastCodeGen
- **Email**: alialfayed.official@gmail.com

---

**Version 1.0.6** | Last Updated: November 2025

*For detailed explanations and examples, see the [User Guide](USER_GUIDE.md)*