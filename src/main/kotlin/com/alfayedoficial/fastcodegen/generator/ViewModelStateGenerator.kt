package com.alfayedoficial.fastcodegen.generator

import com.alfayedoficial.fastcodegen.settings.FastCodeGenSettings
import com.alfayedoficial.fastcodegen.utils.FileUtils.createKotlinFile
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class ViewModelStateGenerator(private val project: Project, private val baseDirectory: PsiDirectory) {

   private val settings = FastCodeGenSettings.getInstance(project)

   fun generate(
      featureName: String,
      enableEvents: Boolean,
      enableRefresh: Boolean,
      enableUIState: Boolean,
      useCases: List<String>
   ) {
      println("=== Starting generation ===")
      println("Feature: $featureName")

      val featureFolder = toCamelCase(featureName)
      val featureClass = toPascalCase(featureName)

      println("Folder: $featureFolder, Class: $featureClass")

      // Create directories
      val featureDir = baseDirectory.findSubdirectory(featureFolder) ?: baseDirectory.createSubdirectory(featureFolder)
      val viewModelDir = featureDir.findSubdirectory("viewmodel") ?: featureDir.createSubdirectory("viewmodel")
      val stateDir = viewModelDir.findSubdirectory("state") ?: viewModelDir.createSubdirectory("state")

      // Get packages
      val basePackage = getPackageName(baseDirectory)
      val fullPackage = "$basePackage.$featureFolder"
      val statePackage = "$fullPackage.viewmodel.state"
      val viewModelPackage = "$fullPackage.viewmodel"

      // Generate State file
      val stateContent = generateStateFile(featureClass, statePackage, enableEvents, enableRefresh, enableUIState)
      createKotlinFile(project, stateDir, "${featureClass}State.kt", stateContent)

      // Generate ViewModel file
      val viewModelContent = generateViewModelFile(featureClass, viewModelPackage, statePackage, enableEvents, enableRefresh, enableUIState, useCases)
      createKotlinFile(project, viewModelDir, "${featureClass}ViewModel.kt", viewModelContent)

      println("=== Generation complete ===")
   }

   private fun generateStateFile(
      featureClass: String,
      statePackage: String,
      enableEvents: Boolean,
      enableRefresh: Boolean,
      enableUIState: Boolean
   ): String {
      val sb = StringBuilder()

      // Package and imports using settings
      sb.appendLine("package $statePackage")
      sb.appendLine()
      sb.appendLine("import ${settings.baseStatePath}")
      if (enableEvents) {
         sb.appendLine("import ${settings.baseEventPath}")
      }
      if (enableUIState) {
         sb.appendLine("import ${settings.baseUIStatePath}")
         if (enableRefresh) {
            sb.appendLine("import ${settings.refreshablePath}")
         }
      }
      sb.appendLine("import ${settings.baseIntentPath}")
      sb.appendLine()

      // Get simple class names
      val baseStateClass = settings.getClassName(settings.baseStatePath)
      val baseEventClass = settings.getClassName(settings.baseEventPath)
      val baseUIStateClass = settings.getClassName(settings.baseUIStatePath)
      val refreshableClass = settings.getClassName(settings.refreshablePath)
      val baseIntentClass = settings.getClassName(settings.baseIntentPath)

      // State
      sb.appendLine("// ═══════════════════════════════════════════════════════════════")
      sb.appendLine("// State")
      sb.appendLine("// ═══════════════════════════════════════════════════════════════")
      sb.appendLine()
      sb.appendLine("sealed class ${featureClass}State : $baseStateClass {")
      sb.appendLine("    data object Idle : ${featureClass}State()")
      sb.appendLine("    data object Loading : ${featureClass}State()")
      sb.appendLine("    data object Success : ${featureClass}State()")
      sb.appendLine("    data class Error(val message: String) : ${featureClass}State()")
      sb.appendLine("}")
      sb.appendLine()

      // Event
      if (enableEvents) {
         sb.appendLine("// ═══════════════════════════════════════════════════════════════")
         sb.appendLine("// Event")
         sb.appendLine("// ═══════════════════════════════════════════════════════════════")
         sb.appendLine()
         sb.appendLine("sealed class ${featureClass}Event : $baseEventClass {")
         sb.appendLine("    data object Loading : ${featureClass}Event()")
         sb.appendLine("    data object Success : ${featureClass}Event()")
         sb.appendLine("    data class Error(val message: String) : ${featureClass}Event()")
         sb.appendLine("}")
         sb.appendLine()
      }

      // UIState
      if (enableUIState) {
         sb.appendLine("// ═══════════════════════════════════════════════════════════════")
         sb.appendLine("// UIState")
         sb.appendLine("// ═══════════════════════════════════════════════════════════════")
         sb.appendLine()
         if (enableRefresh) {
            sb.appendLine("data class ${featureClass}UIState(")
            sb.appendLine("    val isRefresh: Boolean = false,")
            sb.appendLine("    val isLoading: Boolean = false,")
            sb.appendLine("    // TODO: Add your UI state properties here")
            sb.appendLine(") : $baseUIStateClass, $refreshableClass {")
            sb.appendLine("    override fun withRefresh(isRefresh: Boolean): $baseUIStateClass {")
            sb.appendLine("        return copy(isRefresh = isRefresh)")
            sb.appendLine("    }")
            sb.appendLine("}")
         } else {
            sb.appendLine("data class ${featureClass}UIState(")
            sb.appendLine("    val isLoading: Boolean = false,")
            sb.appendLine("    // TODO: Add your UI state properties here")
            sb.appendLine(") : $baseUIStateClass")
         }
         sb.appendLine()
      }

      // Intent
      sb.appendLine("// ═══════════════════════════════════════════════════════════════")
      sb.appendLine("// Intent")
      sb.appendLine("// ═══════════════════════════════════════════════════════════════")
      sb.appendLine()
      sb.appendLine("sealed class ${featureClass}Intent : $baseIntentClass {")
      sb.appendLine("    data object ClearState : ${featureClass}Intent()")
      sb.appendLine("    data object Load${featureClass} : ${featureClass}Intent()")
      if (enableRefresh) sb.appendLine("    data object RefreshRequest : ${featureClass}Intent()")
      sb.appendLine("    // TODO: Add your custom intents here")
      sb.appendLine("}")

      return sb.toString()
   }

   private fun generateViewModelFile(
      featureClass: String,
      viewModelPackage: String,
      statePackage: String,
      enableEvents: Boolean,
      enableRefresh: Boolean,
      enableUIState: Boolean,
      useCases: List<String>
   ): String {
      val sb = StringBuilder()

      // Package and imports using settings
      sb.appendLine("package $viewModelPackage")
      sb.appendLine()
      sb.appendLine("import ${settings.appViewModelPath}")
      sb.appendLine("import ${settings.viewModelConfigPath}")
      sb.appendLine("import $statePackage.${featureClass}Intent")
      sb.appendLine("import $statePackage.${featureClass}State")
      if (enableEvents) {
         sb.appendLine("import $statePackage.${featureClass}Event")
      }
      if (enableUIState) {
         sb.appendLine("import $statePackage.${featureClass}UIState")
      }
      sb.appendLine()

      // Get simple class names
      val appViewModelClass = settings.getClassName(settings.appViewModelPath)
      val viewModelConfigClass = settings.getClassName(settings.viewModelConfigPath)

      val eventType = if (enableEvents) "${featureClass}Event" else "Unit"
      val uiStateType = if (enableUIState) "${featureClass}UIState" else "Unit"
      val uiStateInit = if (enableUIState) "${featureClass}UIState()" else "Unit"

      // Class declaration
      if (useCases.isNotEmpty()) {
         sb.appendLine("class ${featureClass}ViewModel(")
         useCases.forEachIndexed { index, useCase ->
            val useCaseVar = useCase.replaceFirstChar { it.lowercase() }
            val comma = if (index < useCases.size - 1) "," else ""
            sb.appendLine("    private val ${useCaseVar}UseCase: ${useCase}UseCase$comma")
         }
         sb.append(") : $appViewModelClass<${featureClass}State, $eventType, $uiStateType, ${featureClass}Intent>(")
      } else {
         sb.append("class ${featureClass}ViewModel() : $appViewModelClass<${featureClass}State, $eventType, $uiStateType, ${featureClass}Intent>(")
      }

      sb.appendLine()
      sb.appendLine("    initialState = ${featureClass}State.Idle,")
      sb.appendLine("    initialUIState = $uiStateInit,")
      sb.appendLine("    config = $viewModelConfigClass(")
      sb.appendLine("        enableRefresh = $enableRefresh,")
      sb.appendLine("        enableEvents = $enableEvents")
      sb.appendLine("    )")
      sb.appendLine(") {")
      sb.appendLine()

      // handleIntent
      sb.appendLine("    override fun handleIntent(intent: ${featureClass}Intent) {")
      sb.appendLine("        when (intent) {")
      sb.appendLine("            is ${featureClass}Intent.ClearState -> setState(${featureClass}State.Idle)")
      sb.appendLine("            is ${featureClass}Intent.Load${featureClass} -> load${featureClass}()")
      if (enableRefresh) sb.appendLine("            is ${featureClass}Intent.RefreshRequest -> refreshRequest { load${featureClass}() }")
      sb.appendLine("        }")
      sb.appendLine("    }")
      sb.appendLine()

      // createErrorState
      sb.appendLine("    override fun createErrorState(message: String): ${featureClass}State {")
      sb.appendLine("        return ${featureClass}State.Error(message)")
      sb.appendLine("    }")

      // createErrorEvent
      if (enableEvents) {
         sb.appendLine()
         sb.appendLine("    override fun createErrorEvent(message: String): ${featureClass}Event {")
         sb.appendLine("        return ${featureClass}Event.Error(message)")
         sb.appendLine("    }")
      }

      sb.appendLine()

      // load function
      sb.appendLine("    private fun load${featureClass}() {")
      sb.appendLine("        launch {")
      sb.appendLine("            setState(${featureClass}State.Loading)")
      sb.appendLine("            // TODO: Implement")
      sb.appendLine("        }")
      sb.appendLine("    }")
      sb.appendLine("}")

      return sb.toString()
   }

   private fun getPackageName(directory: PsiDirectory): String {
      val sourceRoot = com.intellij.openapi.roots.ProjectRootManager.getInstance(project)
         .fileIndex
         .getSourceRootForFile(directory.virtualFile)

      if (sourceRoot != null) {
         val relativePath = com.intellij.openapi.util.io.FileUtil.getRelativePath(
            sourceRoot.path,
            directory.virtualFile.path,
            '/'
         )
         return relativePath?.replace('/', '.') ?: ""
      }

      val sourcePath = directory.virtualFile.path
      val parts = sourcePath.split("/")
      val kotlinIndex = parts.indexOfLast { it == "kotlin" || it == "java" }
      if (kotlinIndex >= 0 && kotlinIndex < parts.size - 1) {
         return parts.drop(kotlinIndex + 1).joinToString(".")
      }
      return ""
   }

   private fun toCamelCase(input: String): String {
      return input.replace(Regex("[^a-zA-Z0-9]"), "").lowercase()
   }

   private fun toPascalCase(input: String): String {
      val cleaned = input.replace(Regex("[^a-zA-Z0-9]"), "").lowercase()
      return cleaned.replaceFirstChar { it.uppercase() }
   }
}