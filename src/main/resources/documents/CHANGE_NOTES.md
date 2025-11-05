# Version 1.0.6 - Screen Generation & Enhanced Architecture

**Release Date:** November 2025

## 🎨 Major Features

### Screen Generation (NEW!)
- **Complete Compose Screen Generation** - Automatically create Jetpack Compose screens with proper structure
- **Three Navigation Types:**
    - **None** - Simple screens without navigation
    - **Simple (Composable)** - Standard navigation using route constants
    - **Type-Safe (SafeType)** - Type-safe navigation with @Serializable and automatic parameter injection
- **ViewModel Integration** - Auto-inject ViewModels with state collection and LaunchedEffect
- **Navigation Parameters** - Define and inject type-safe navigation parameters

### Navigation Integration
- Built-in support for multiple navigation patterns
- Automatic parameter injection for type-safe routes
- Back navigation handling
- LaunchedEffect for initialization

## 🏗️ Architecture & Refactoring

- **Modular Panel System** - Refactored UI into separate panels (Screen, ViewModel, Repository, Feature)
- **Generation Manager** - Centralized business logic for all generation types
- **80% Code Reduction** - Main dialog simplified from 800+ to ~150 lines
- **StringUtils Utility** - Added toCamelCase, toPascalCase, toSnakeCase methods
- **Better Organization** - Extracted utilities for improved maintainability

## ⚙️ Settings & Configuration

- **Comprehensive Settings Dialog** - New UI with organized sections
- **Browse & Auto-Detect** - Click 📁 to browse files and auto-detect package paths
- **Dynamic Imports** - All imports based on your configuration
- **Navigation Utilities** - Configure composableRoute and composableSafeType paths
- **Settings Validation** - Built-in validation before generation
- **Fixed File Chooser** - Resolved deprecated API usage
- **Quick Access** - Settings button (⚙️) in main dialog

## 🔧 Enhanced Features

- **Include Load Method** - Optional load() method for ViewModel initialization
- **Optional Repository Methods** - Methods, parameters, and return types can be empty
- **Enhanced FeatureGenerator** - Support for optional Screen and ViewModel creation
- **Improved Parameter Injection** - Better handling of navigation parameters
- **Enhanced Documentation** - 7 comprehensive guides accessible from plugin
- **Better Validation** - Improved error messages throughout
- **UI Constants** - Centralized strings and dimensions

## 🐛 Bug Fixes

- Fixed file chooser behavior in settings dialog
- Improved path detection and validation
- Enhanced error handling in generation process
- Fixed imports for repositories without HttpClient
- Resolved state handling when events are disabled

## 📚 Documentation

New comprehensive documentation:
- **README.md** - Complete overview
- **USER_GUIDE.md** - Detailed tutorials
- **QUICK_REFERENCE.md** - Cheat sheet
- **IMPLEMENTATION_STEPS.md** - Setup guide
- **PLUGIN_SUMMARY.md** - Feature overview
- **FILES_TO_DOWNLOAD.md** - Required files
- **INDEX.md** - Documentation hub

## 🎯 Getting Started

1. **Configure Settings** - Click ⚙️ in FastCodeGen dialog
2. **Browse Base Classes** - Use 📁 to auto-detect paths
3. **Configure Navigation** - Set composableRoute and composableSafeType paths
4. **Start Generating** - Right-click package → New → FastCodeGen

## 💡 New in Generated Code

- Complete Composable screen functions with navigation
- ViewModels with optional load() methods and init blocks
- Type-safe navigation parameter injection
- Dynamic imports matching your configured paths

## 🔄 Migration from 1.0.0

1. Open Settings (⚙️) and configure all required paths
2. Use browse buttons (📁) for automatic path detection
3. Configure navigation utilities for screen generation
4. Previous generated code continues to work

## ⚠️ Breaking Changes

- **Settings Required** - Must configure settings before first use
- **Navigation Utilities** - Screen generation requires composableRoute and/or composableSafeType

## 🚀 What's Next?

Access documentation from the plugin:
- 📚 Help Button - All guides in one place
- 🔧 Implementation Steps - Setup guide
- 📖 User Guide - Detailed tutorials
- ⚡ Quick Reference - Quick lookup

---

# Version 1.0.0 - Initial Release

**Release Date:** October 2025

## Features

- 🎉 Initial Release
- ⚡ ViewModel State Generation
- 📦 Repository Generation
- 🏗️ Full Feature Generation
- 🔧 Configuration Options (Events, Refresh, UIState)
- 📝 Use Case Integration
- 🌐 HTTP Client Support
- ✅ K2 Compiler Support
- 📋 Custom Repository Methods