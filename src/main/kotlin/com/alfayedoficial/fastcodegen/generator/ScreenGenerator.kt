package com.alfayedoficial.fastcodegen.generator

import com.alfayedoficial.fastcodegen.utils.StringUtils
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory

class ScreenGenerator(
   private val project: Project,
   private val baseDirectory: PsiDirectory
) {

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
      injectViewModel: Boolean = false  // NEW PARAMETER
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

      // Generate Screen file
      val screenContent = generateScreenFile(
         featureClass = featureClass,
         packageName = screenPackage,
         hasNavigationBack = hasNavigationBack,
         isTypeSafe = navigationType == NavigationType.TYPE_SAFE,
         navParameters = navParameters,
         injectViewModel = injectViewModel  // PASS IT HERE
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
      hasNavigationBack: Boolean,
      isTypeSafe: Boolean,
      navParameters: List<NavParameter>,
      injectViewModel: Boolean  // NEW PARAMETER
   ): String {
      val sb = StringBuilder()

      sb.appendLine("package $packageName")
      sb.appendLine()
      sb.appendLine("import androidx.compose.runtime.Composable")
      if (injectViewModel) {
         sb.appendLine("import org.koin.androidx.compose.koinViewModel")
      }
      sb.appendLine()

      // Route function
      sb.appendLine("@Composable")
      sb.append("internal fun ${featureClass}Route(")

      val params = mutableListOf<String>()
      if (isTypeSafe && navParameters.isNotEmpty()) {
         navParameters.forEach { param ->
            params.add("${param.name}: ${param.type}")
         }
      }
      if (injectViewModel) {
         params.add("viewModel: ${featureClass}ViewModel = koinViewModel()")
      }
      if (hasNavigationBack) {
         params.add("navigationBack: () -> Unit")
      }

      if (params.isNotEmpty()) {
         sb.appendLine()
         params.forEachIndexed { index, param ->
            val comma = if (index < params.size - 1) "," else ""
            sb.appendLine("    $param$comma")
         }
      }
      sb.appendLine(") {")
      sb.appendLine()

      // Call Screen
      sb.append("    ${featureClass}Screen(")
      if (params.isNotEmpty()) {
         sb.appendLine()
         params.forEachIndexed { index, param ->
            val paramName = param.split(":").first().trim().split(" = ").first().trim()
            val comma = if (index < params.size - 1) "," else ""
            sb.appendLine("        $paramName = $paramName$comma")
         }
         sb.append("    ")
      }
      sb.appendLine(")")

      sb.appendLine("}")
      sb.appendLine()

      // Screen function
      sb.appendLine("@Composable")
      sb.append("private fun ${featureClass}Screen(")

      if (params.isNotEmpty()) {
         sb.appendLine()
         params.forEachIndexed { index, param ->
            val comma = if (index < params.size - 1) "," else ""
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
      sb.appendLine("import androidx.navigation.compose.composable")
      sb.appendLine("import $screenPackage.${featureClass}Route")
      sb.appendLine()

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
      sb.appendLine("    composable($routeConst) {")
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
      sb.appendLine("import androidx.navigation.compose.composable")
      sb.appendLine("import androidx.navigation.toRoute")
      sb.appendLine("import kotlinx.serialization.Serializable")
      sb.appendLine("import $screenPackage.${featureClass}Route")
      sb.appendLine()

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
            val comma = if (index < navParameters.size - 1) "," else ""
            sb.appendLine("    ${param.name}: ${param.type}$comma")
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
         sb.appendLine("    navigateBack: () -> Unit,")
      }
      sb.appendLine(") {")
      sb.appendLine("    composable<${featureClass}Destination> { backStackEntry ->")
      if (navParameters.isNotEmpty()) {
         sb.appendLine("        val args: ${featureClass}Destination = backStackEntry.toRoute()")
      }
      sb.append("        ${featureClass}Route(")
      if (navParameters.isNotEmpty() || hasNavigationBack) {
         sb.appendLine()
         if (navParameters.isNotEmpty()) {
            navParameters.forEach { param ->
               sb.appendLine("            ${param.name} = args.${param.name},")
            }
         }
         if (hasNavigationBack) {
            sb.appendLine("            navigateBack = navigateBack,")
         }
         sb.append("        ")
      }
      sb.appendLine(")")
      sb.appendLine("    }")
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