package com.alfayedoficial.fastcodegen.generator

import com.alfayedoficial.fastcodegen.settings.FastCodeGenSettings
import com.alfayedoficial.fastcodegen.utils.StringUtils
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory

class ScreenGenerator(
   private val project: Project,
   private val baseDirectory: PsiDirectory
) {

   private val settings = FastCodeGenSettings.getInstance(project)

   enum class NavigationType {
      NONE,           // No navigation file
      SIMPLE,         // Simple route with constant
      TYPE_SAFE       // Type-safe with @Serializable
   }

   data class NavParameter(
      val name: String,
      val type: String
   )

   fun generate(
      featureName: String,
      hasNavigationBack: Boolean,
      navigationType: NavigationType,
      navParameters: List<NavParameter>,
      injectViewModel: Boolean = false
   ) {
      println("=== Starting Screen generation ===")
      println("Feature: $featureName")

      val featureFolder = StringUtils.toCamelCase(featureName)
      val featureClass = StringUtils.toPascalCase(featureName)
      val routeConst = StringUtils.toSnakeCase(featureName).uppercase() + "_ROUTE"

      println("Folder: $featureFolder, Class: $featureClass")

      // Create directories
      val featureDir = findOrCreateFolder(baseDirectory, featureFolder)
      println("Feature dir: ${featureDir.virtualFile.path}")

      // Get packages
      val basePackage = getPackageName(baseDirectory)
      val screenPackage = "$basePackage.$featureFolder"
      val viewModelPackage = "$basePackage.$featureFolder.viewmodel"
      val statePackage = "$basePackage.$featureFolder.viewmodel.state"

      // Generate Screen file
      val screenContent = generateScreenFile(
         featureClass = featureClass,
         packageName = screenPackage,
         viewModelPackage = viewModelPackage,
         statePackage = statePackage,
         hasNavigationBack = hasNavigationBack,
         isTypeSafe = navigationType == NavigationType.TYPE_SAFE,
         navParameters = navParameters,
         injectViewModel = injectViewModel
      )
      createKotlinFile(
         project = project,
         directory = featureDir,
         fileName = "${featureClass}Screen.kt",
         content = screenContent
      )
      println("Screen file created")

      // Generate Navigation file if needed
      if (navigationType != NavigationType.NONE) {
         val navigationDir = findOrCreateFolder(featureDir, "navigation")
         println("Navigation dir: ${navigationDir.virtualFile.path}")

         val navigationPackage = "$basePackage.$featureFolder.navigation"
         val navigationContent = when (navigationType) {
            NavigationType.SIMPLE -> generateSimpleNavigation(
               featureClass,
               featureFolder,
               routeConst,
               navigationPackage,
               screenPackage,
               hasNavigationBack
            )
            NavigationType.TYPE_SAFE -> generateTypeSafeNavigation(
               featureClass,
               featureFolder,
               navigationPackage,
               screenPackage,
               hasNavigationBack,
               navParameters
            )
            else -> ""
         }

         createKotlinFile(project, navigationDir, "${featureClass}Navigation.kt", navigationContent)
         println("Navigation file created")
      }

      println("=== Screen generation complete ===")
   }

   private fun generateScreenFile(
      featureClass: String,
      packageName: String,
      viewModelPackage: String,
      statePackage: String,
      hasNavigationBack: Boolean,
      isTypeSafe: Boolean,
      navParameters: List<NavParameter>,
      injectViewModel: Boolean
   ): String {
      val sb = StringBuilder()

      sb.appendLine("package $packageName")
      sb.appendLine()
      sb.appendLine("import androidx.compose.runtime.Composable")
      if (injectViewModel) {
         sb.appendLine("import $viewModelPackage.${featureClass}ViewModel")
         sb.appendLine("import $statePackage.${featureClass}Intent")
         sb.appendLine("import $statePackage.${featureClass}State")
         sb.appendLine("import $statePackage.${featureClass}UIState")
         sb.appendLine("import kotlinx.coroutines.flow.StateFlow")
         sb.appendLine("import org.koin.compose.viewmodel.koinViewModel")
      }
      sb.appendLine()

      // Route function
      sb.appendLine("@Composable")
      sb.append("internal fun ${featureClass}Route(")

      val routeParams = mutableListOf<String>()
      if (isTypeSafe && navParameters.isNotEmpty()) {
         navParameters.forEach { param ->
            routeParams.add("${param.name}: ${param.type}")
         }
      }
      if (injectViewModel) {
         routeParams.add("viewModel: ${featureClass}ViewModel = koinViewModel()")
      }
      if (hasNavigationBack) {
         routeParams.add("navigationBack: () -> Unit")
      }

      if (routeParams.isNotEmpty()) {
         sb.appendLine()
         routeParams.forEachIndexed { index, param ->
            val comma = if (index < routeParams.size - 1) "," else ""
            sb.appendLine("    $param$comma")
         }
      }
      sb.appendLine(") {")
      sb.appendLine()

      // Call Screen with extracted flows
      sb.append("    ${featureClass}Screen(")
      if (routeParams.isNotEmpty()) {
         sb.appendLine()

         // Add nav parameters if any
         if (isTypeSafe && navParameters.isNotEmpty()) {
            navParameters.forEach { param ->
               sb.appendLine("        ${param.name} = ${param.name},")
            }
         }

         // Add ViewModel flows if injected
         if (injectViewModel) {
            sb.appendLine("        apiState = viewModel.state,")
            sb.appendLine("        uiState = viewModel.uiState,")
            sb.appendLine("        onIntent = viewModel::handleIntent,")
         }

         // Add navigation back
         if (hasNavigationBack) {
            sb.appendLine("        navigationBack = navigationBack")
         }

         sb.append("    ")
      }
      sb.appendLine(")")

      sb.appendLine("}")
      sb.appendLine()

      // Screen function - receives flows, not ViewModel
      sb.appendLine("@Composable")
      sb.append("private fun ${featureClass}Screen(")

      val screenParams = mutableListOf<String>()
      if (isTypeSafe && navParameters.isNotEmpty()) {
         navParameters.forEach { param ->
            screenParams.add("${param.name}: ${param.type}")
         }
      }
      if (injectViewModel) {
         screenParams.add("apiState: StateFlow<${featureClass}State>")
         screenParams.add("uiState: StateFlow<${featureClass}UIState>")
         screenParams.add("onIntent: (${featureClass}Intent) -> Unit = {}")
      }
      if (hasNavigationBack) {
         screenParams.add("navigationBack: () -> Unit")
      }

      if (screenParams.isNotEmpty()) {
         sb.appendLine()
         screenParams.forEachIndexed { index, param ->
            val comma = if (index < screenParams.size - 1) "," else ""
            sb.appendLine("    $param$comma")
         }
      }
      sb.appendLine(") {")
      sb.appendLine("    // TODO: Implement your screen here")
      sb.appendLine("}")

      return sb.toString()
   }

   private fun generateSimpleNavigation(
      featureClass: String,
      featureFolder: String,
      routeConst: String,
      navigationPackage: String,
      screenPackage: String,
      hasNavigationBack: Boolean
   ): String {
      val sb = StringBuilder()

      sb.appendLine("package $navigationPackage")
      sb.appendLine()
      sb.appendLine("import androidx.navigation.NavController")
      sb.appendLine("import androidx.navigation.NavGraphBuilder")
      sb.appendLine("import androidx.navigation.NavOptions")
      sb.appendLine("import ${settings.composableRoutePath}")
      sb.appendLine("import $screenPackage.${featureClass}Route")
      sb.appendLine()

      // Get function name from path
      val composableRouteName = settings.getClassName(settings.composableRoutePath)

      // Route constant
      sb.appendLine("const val $routeConst = \"${featureFolder}_route\"")
      sb.appendLine()

      // NavController extension
      sb.appendLine("fun NavController.navigateTo$featureClass(navOptions: NavOptions? = null) {")
      sb.appendLine("    navigate($routeConst, navOptions)")
      sb.appendLine("}")
      sb.appendLine()

      // NavGraphBuilder extension
      sb.appendLine("fun NavGraphBuilder.${featureFolder}Screen(")
      if (hasNavigationBack) {
         sb.appendLine("    navigationBack: () -> Unit,")
      }
      sb.appendLine(") {")
      sb.appendLine("    $composableRouteName($routeConst) {")
      sb.append("        ${featureClass}Route(")
      if (hasNavigationBack) {
         sb.append("navigationBack = navigationBack")
      }
      sb.appendLine(")")
      sb.appendLine("    }")
      sb.appendLine("}")

      return sb.toString()
   }

   private fun generateTypeSafeNavigation(
      featureClass: String,
      featureFolder: String,
      navigationPackage: String,
      screenPackage: String,
      hasNavigationBack: Boolean,
      navParameters: List<NavParameter>
   ): String {
      val sb = StringBuilder()

      sb.appendLine("package $navigationPackage")
      sb.appendLine()
      sb.appendLine("import androidx.navigation.NavController")
      sb.appendLine("import androidx.navigation.NavGraphBuilder")
      sb.appendLine("import androidx.navigation.NavOptions")
      sb.appendLine("import ${settings.composableSafeTypePath}")
      sb.appendLine("import kotlinx.serialization.Serializable")
      sb.appendLine("import $screenPackage.${featureClass}Route")
      sb.appendLine()

      // Get function name from path
      val composableSafeTypeName = settings.getClassName(settings.composableSafeTypePath)

      // Destination data class
      sb.append("@Serializable")
      sb.appendLine()
      sb.append("data class ${featureClass}Destination(")
      if (navParameters.isNotEmpty()) {
         sb.appendLine()
         navParameters.forEachIndexed { index, param ->
            val comma = if (index < navParameters.size - 1) "," else ""
            sb.appendLine("    val ${param.name}: ${param.type}$comma")
         }
      }
      sb.appendLine(")")
      sb.appendLine()

      // NavController extension
      sb.append("fun NavController.navigateTo$featureClass(")
      if (navParameters.isNotEmpty()) {
         sb.appendLine()
         navParameters.forEachIndexed { index, param ->
            sb.appendLine("    ${param.name}: ${param.type},")
         }
         sb.appendLine("    navOptions: NavOptions? = null,")
      } else {
         sb.append("navOptions: NavOptions? = null")
      }
      sb.appendLine(") {")
      sb.append("    navigate(${featureClass}Destination(")
      if (navParameters.isNotEmpty()) {
         navParameters.forEachIndexed { index, param ->
            val comma = if (index < navParameters.size - 1) ", " else ""
            sb.append("${param.name}$comma")
         }
      }
      sb.appendLine("), navOptions)")
      sb.appendLine("}")
      sb.appendLine()

      // NavGraphBuilder extension
      sb.appendLine("fun NavGraphBuilder.${featureFolder}Screen(")
      if (hasNavigationBack) {
         sb.appendLine("    navigationBack: () -> Unit,")
      }
      sb.appendLine(") {")
      sb.appendLine()
      sb.appendLine("    $composableSafeTypeName<${featureClass}Destination>(")
      sb.appendLine("        content = { args, _ ->")
      sb.append("            ${featureClass}Route(")
      if (navParameters.isNotEmpty() || hasNavigationBack) {
         sb.appendLine()
         if (navParameters.isNotEmpty()) {
            navParameters.forEach { param ->
               sb.appendLine("                ${param.name} = args.${param.name},")
            }
         }
         if (hasNavigationBack) {
            sb.appendLine("                navigationBack = navigationBack,")
         }
         sb.append("            ")
      }
      sb.appendLine(")")
      sb.appendLine("        }")
      sb.appendLine("    )")
      sb.appendLine("}")

      return sb.toString()
   }

   private fun findOrCreateFolder(parent: PsiDirectory, name: String): PsiDirectory {
      return parent.findSubdirectory(name) ?: parent.createSubdirectory(name)
   }

   private fun createKotlinFile(project: Project, directory: PsiDirectory, fileName: String, content: String) {
      val existingFile = directory.findFile(fileName)
      existingFile?.delete()

      val fileType = com.intellij.openapi.fileTypes.FileTypeManager.getInstance().getFileTypeByExtension("kt")
      val psiFile = PsiFileFactory.getInstance(project).createFileFromText(fileName, fileType, content)
      directory.add(psiFile)
   }

   private fun getPackageName(directory: PsiDirectory): String {
      val sourceRoot = com.intellij.openapi.roots.ProjectRootManager.getInstance(project)
         .fileIndex
         .getSourceRootForFile(directory.virtualFile)

      if (sourceRoot != null) {
         val relativePath = FileUtil.getRelativePath(
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
}