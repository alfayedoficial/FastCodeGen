import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
   id("java")
   id("org.jetbrains.kotlin.jvm") version "2.1.0"
   id("org.jetbrains.intellij.platform") version "2.5.0"
}

group = "com.alfayedoficial"
version = "1.0.6"

repositories {
   mavenCentral()
   intellijPlatform {
      defaultRepositories()
   }
}

dependencies {
   intellijPlatform {
      create("IC", "2025.1")
      bundledPlugin("org.jetbrains.kotlin")
      testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
   }
}

intellijPlatform {
   pluginConfiguration {
      ideaVersion {
         sinceBuild = "242"
         untilBuild = "252.*"
      }

      changeNotes = """
  <h2>Version 1.0.6 - Screen Generation & Enhanced Architecture (November 2025)</h2>
  
  <h3>🎨 Major Features</h3>
  <ul>
    <li><strong>Screen Generation (NEW!)</strong> - Complete Jetpack Compose screen generation with automatic structure and state handling</li>
    <li><strong>Navigation Integration</strong> - Built-in support for three navigation types:
      <ul>
        <li><strong>None</strong> - Simple screens without navigation parameters</li>
        <li><strong>Simple (Composable)</strong> - Standard navigation using route constants</li>
        <li><strong>Type-Safe (SafeType)</strong> - Type-safe navigation with @Serializable routes and automatic parameter injection</li>
      </ul>
    </li>
    <li><strong>ViewModel Integration in Screens</strong> - Automatically inject ViewModels with state collection, LaunchedEffect initialization, and proper state handling</li>
    <li><strong>Navigation Parameters</strong> - Define and inject navigation parameters for type-safe routing with automatic code generation</li>
  </ul>
  
  <h3>🏗️ Architecture & Refactoring</h3>
  <ul>
    <li><strong>Modular Panel System</strong> - Refactored UI into separate panels: ScreenPanel, ViewModelPanel, RepositoryPanel, and FeaturePanel for better maintainability</li>
    <li><strong>Generation Manager</strong> - Centralized business logic with GenerationManager class handling all generation types</li>
    <li><strong>80% Code Reduction</strong> - Main dialog simplified from 800+ lines to approximately 150 lines through better architecture</li>
    <li><strong>StringUtils Utility</strong> - Added toCamelCase, toPascalCase, and toSnakeCase methods for consistent naming conventions</li>
    <li><strong>FileUtils & PackageUtils</strong> - Extracted utility functions for better code organization and reusability</li>
  </ul>
  
  <h3>⚙️ Settings & Configuration</h3>
  <ul>
    <li><strong>Comprehensive Settings Dialog</strong> - New settings UI with organized sections for ViewModel, State, and Navigation configurations</li>
    <li><strong>Browse & Auto-Detect</strong> - Click 📁 button next to any field to browse files and automatically detect full package paths</li>
    <li><strong>Dynamic Imports</strong> - All generated code now uses imports based on your configuration instead of hardcoded paths</li>
    <li><strong>Navigation Utilities Configuration</strong> - Added settings for composableRoute and composableSafeType paths (required for screen generation)</li>
    <li><strong>Settings Validation</strong> - Built-in validation ensures all required paths are configured before code generation</li>
    <li><strong>Fixed File Chooser</strong> - Resolved deprecated FileChooserDescriptor usage with proper constructor implementation</li>
    <li><strong>Settings Button in Dialog</strong> - Quick access to settings via ⚙️ button directly in the main FastCodeGen dialog</li>
  </ul>
  
  <h3>🔧 Enhanced Features</h3>
  <ul>
    <li><strong>Include Load Method</strong> - New option to add optional load() method to ViewModels for automatic initialization with init block</li>
    <li><strong>Optional Repository Methods</strong> - Repository methods, parameters, and return types are now fully optional:
      <ul>
        <li>Method name can be empty to skip method generation</li>
        <li>Return type defaults to Unit if not specified</li>
        <li>Parameters can be empty for methods without parameters</li>
      </ul>
    </li>
    <li><strong>Enhanced FeatureGenerator</strong> - Updated to support optional Screen and ViewModel creation with flexible configuration</li>
    <li><strong>Improved Parameter Injection</strong> - Better handling of navigation parameters in screen generation with proper type checking</li>
    <li><strong>Enhanced Documentation System</strong> - Comprehensive documentation accessible from Help menu with 7 complete guides</li>
    <li><strong>Better Validation & Error Messages</strong> - Improved error handling throughout generation process with descriptive messages</li>
    <li><strong>UI Constants</strong> - Centralized UI strings and dimensions for consistent styling across the plugin</li>
  </ul>
  
  <h3>🐛 Bug Fixes</h3>
  <ul>
    <li>Fixed file chooser behavior in settings dialog - replaced deprecated API with proper FileChooserDescriptor constructor</li>
    <li>Improved path detection and validation - better handling of edge cases in package path extraction</li>
    <li>Enhanced error handling in generation process - better error messages and graceful failure handling</li>
    <li>Fixed imports for repositories without HttpClient - correct generation when HttpClient is disabled</li>
    <li>Resolved state handling in ViewModel generation - proper NoEvent usage when events are disabled</li>
  </ul>
  
  <h3>📚 Documentation</h3>
  <ul>
    <li><strong>README.md</strong> - Complete overview with installation, features, and quick start guide</li>
    <li><strong>USER_GUIDE.md</strong> - Comprehensive tutorials with step-by-step examples for all generation types</li>
    <li><strong>QUICK_REFERENCE.md</strong> - Cheat sheet for quick lookup of syntax, options, and patterns</li>
    <li><strong>IMPLEMENTATION_STEPS.md</strong> - Detailed setup guide from prerequisites to first generation</li>
    <li><strong>PLUGIN_SUMMARY.md</strong> - High-level overview of capabilities, architecture, and roadmap</li>
    <li><strong>FILES_TO_DOWNLOAD.md</strong> - Complete list of required base classes and dependencies</li>
    <li><strong>INDEX.md</strong> - Navigation hub for all documentation with organized access paths</li>
  </ul>
  
  <h3>🎯 Getting Started with 1.0.6</h3>
  <ol>
    <li><strong>Configure Settings</strong> - Click ⚙️ button in FastCodeGen dialog or go to Tools → FastCodeGen Documentation → Settings</li>
    <li><strong>Browse Base Classes</strong> - Use 📁 button to browse and auto-detect paths for AppViewModel, ViewModelConfig, BaseState, BaseEvent, BaseUIState, Refreshable, BaseIntent</li>
    <li><strong>Configure Navigation</strong> - Set up composableRoute and composableSafeType paths for screen generation features</li>
    <li><strong>Start Generating</strong> - Right-click any package → New → FastCodeGen → Choose generation type → Configure & Generate!</li>
  </ol>
  
  <h3>💡 New in Generated Code</h3>
  <ul>
    <li><strong>Screen Files</strong> - Complete Composable functions with navigation, ViewModel integration, and state handling</li>
    <li><strong>Load Methods</strong> - ViewModels can now include automatic initialization with load() method called from init block</li>
    <li><strong>Type-Safe Navigation</strong> - Screens generated with proper parameter injection for type-safe navigation</li>
    <li><strong>Dynamic Imports</strong> - All imports now match your configured base class paths</li>
  </ul>
  
  <h3>🔄 Migration from 1.0.0</h3>
  <p>If you're upgrading from version 1.0.0:</p>
  <ol>
    <li>Open Settings dialog (⚙️) and configure all required paths</li>
    <li>Use browse buttons (📁) to automatically detect package paths</li>
    <li>Configure navigation utilities (composableRoute, composableSafeType) if using screen generation</li>
    <li>Previously generated code will continue to work - new generations will use your configured paths</li>
  </ol>
  
  <h3>⚠️ Breaking Changes</h3>
  <ul>
    <li><strong>Settings Required</strong> - Plugin now requires settings configuration before first use. Configure once via ⚙️ button.</li>
    <li><strong>Navigation Utilities</strong> - Screen generation requires composableRoute and/or composableSafeType functions in your project</li>
  </ul>
  
  <h3>🚀 What's Next?</h3>
  <p>Access comprehensive documentation from the plugin:</p>
  <ul>
    <li>📚 <strong>Help Button</strong> - Click in FastCodeGen dialog for quick access to all guides</li>
    <li>🔧 <strong>Implementation Steps</strong> - Step-by-step setup guide</li>
    <li>📖 <strong>User Guide</strong> - Detailed tutorials with examples</li>
    <li>⚡ <strong>Quick Reference</strong> - Cheat sheet for quick lookup</li>
  </ul>
  
  <hr/>
  
  <h2>Version 1.0.0 - Initial Release (October 2024)</h2>
  <ul>
    <li>🎉 <strong>Initial Release</strong> - First public version of FastCodeGen</li>
    <li>⚡ <strong>ViewModel State Generation</strong> - Generate complete ViewModel with State, Event, UIState, and Intent sealed classes</li>
    <li>📦 <strong>Repository Generation</strong> - Create repository interfaces and implementations with custom methods</li>
    <li>🏗️ <strong>Full Feature Generation</strong> - Generate complete features combining ViewModel and Repository</li>
    <li>🔧 <strong>Configuration Options</strong> - Enable/disable Events, Refresh, and UIState as needed</li>
    <li>📝 <strong>Use Case Integration</strong> - Support for injecting use cases into ViewModels</li>
    <li>🌐 <strong>HTTP Client Support</strong> - Optional Ktor HttpClient injection in repositories</li>
    <li>✅ <strong>K2 Compiler Support</strong> - Full compatibility with Kotlin 2.0+ and K2 compiler mode</li>
    <li>📋 <strong>Custom Repository Methods</strong> - Define methods with parameters and return types</li>
  </ul>
""".trimIndent()
   }
}

tasks {
   // Set the JVM compatibility versions
   withType<JavaCompile> {
      sourceCompatibility = "21"
      targetCompatibility = "21"
   }
   withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
   }
}