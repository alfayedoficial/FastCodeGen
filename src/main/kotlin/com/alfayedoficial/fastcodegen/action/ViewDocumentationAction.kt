package com.alfayedoficial.fastcodegen.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.io.InputStreamReader

/**
 * Action to view documentation files in the IDE
 * Shows documentation from plugin resources
 *
 * @author Ali Al-Shahat Ali
 */
class ViewDocumentationAction : AnAction() {

    companion object {
        private val docFiles = mapOf(
            "README.md" to "FastCodeGen - README",
            "USER_GUIDE.md" to "FastCodeGen - User Guide",
            "QUICK_REFERENCE.md" to "FastCodeGen - Quick Reference",
            "IMPLEMENTATION_STEPS.md" to "FastCodeGen - Implementation",
            "PLUGIN_SUMMARY.md" to "FastCodeGen - Plugin Summary",
            "INDEX.md" to "FastCodeGen - Index",
            "FILES_TO_DOWNLOAD.md" to "FastCodeGen - Files to Download"
        )

        /**
         * Open documentation file - can be called from anywhere
         */
        fun openDocumentation(project: Project, fileName: String) {
            val displayName = docFiles[fileName] ?: "FastCodeGen - Documentation"

            try {
                // Try to read from plugin resources
                // Files are in: src/main/resources/documents/
                val resourcePath = "/documents/$fileName"
                val inputStream = ViewDocumentationAction::class.java.getResourceAsStream(resourcePath)

                if (inputStream != null) {
                    val content = InputStreamReader(inputStream).use { it.readText() }

                    // Show in custom dialog with markdown rendering
                    DocumentationViewerDialog(project, displayName, content).show()
                } else {
                    // Fallback: Show helpful message
                    showDocumentationNotFound(project, fileName, displayName)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showError(project, "Could not open documentation: ${e.message}")
            }
        }

        private fun showDocumentationNotFound(project: Project, fileName: String, displayName: String) {
            val message = """
                Documentation: $displayName
                
                📚 Complete documentation available on GitHub:
                https://github.com/alfayedoficial/FastCodeGen
                
                📞 Support:
                • Email: alialfayed.official@gmail.com
                • LinkedIn: linkedin.com/in/alfayedoficial
            """.trimIndent()

            Messages.showInfoMessage(project, message, displayName)
        }

        private fun showError(project: Project, message: String) {
            Messages.showErrorDialog(project, message, "Error")
        }
    }

    private val actionDocFiles = mapOf(
        "com.alfayedoficial.FastCodeGen.ViewReadme" to "README.md",
        "com.alfayedoficial.FastCodeGen.ViewUserGuide" to "USER_GUIDE.md",
        "com.alfayedoficial.FastCodeGen.ViewQuickRef" to "QUICK_REFERENCE.md",
        "com.alfayedoficial.FastCodeGen.ViewImplementation" to "IMPLEMENTATION_STEPS.md",
        "com.alfayedoficial.FastCodeGen.ViewPluginSummary" to "PLUGIN_SUMMARY.md",
        "com.alfayedoficial.FastCodeGen.ViewIndex" to "INDEX.md"
    )

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        // Get action ID from the event
        val actualActionId = e.actionManager.getId(this) ?: return
        val fileName = actionDocFiles[actualActionId] ?: return

        openDocumentation(project, fileName)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }
}