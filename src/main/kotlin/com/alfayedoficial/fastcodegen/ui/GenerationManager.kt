package com.alfayedoficial.fastcodegen.ui

import com.alfayedoficial.fastcodegen.generator.FeatureGenerator
import com.alfayedoficial.fastcodegen.generator.RepoGenerator
import com.alfayedoficial.fastcodegen.generator.ViewModelStateGenerator
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory

/**
 * Manages code generation operations
 */
class GenerationManager(
    private val project: Project,
    private val directory: PsiDirectory
) {
    
    fun generateViewModel(
        featureName: String,
        enableEvents: Boolean,
        enableRefresh: Boolean,
        enableUIState: Boolean,
        useCases: List<String>
    ): Boolean {
        return executeGeneration("ViewModel State", featureName) {
            ViewModelStateGenerator(project, directory).generate(
                featureName,
                enableEvents,
                enableRefresh,
                enableUIState,
                useCases
            )
        }
    }
    
    fun generateRepository(
        featureName: String,
        methods: List<RepoGenerator.RepoMethod>,
        needsHttpClient: Boolean
    ): Boolean {
        return executeGeneration("Repository", featureName) {
            RepoGenerator(project, directory).generate(
                featureName,
                methods,
                needsHttpClient
            )
        }
    }
    
    fun generateFeature(
        featureName: String,
        enableEvents: Boolean,
        enableRefresh: Boolean,
        enableUIState: Boolean,
        useCases: List<String>,
        methods: List<RepoGenerator.RepoMethod>,
        needsHttpClient: Boolean
    ): Boolean {
        return executeGeneration("Full Feature", featureName) {
            FeatureGenerator(project, directory).generate(
                featureName,
                enableEvents,
                enableRefresh,
                enableUIState,
                useCases,
                methods,
                needsHttpClient
            )
        }
    }
    
    private fun executeGeneration(type: String, featureName: String, action: () -> Unit): Boolean {
        return try {
            ApplicationManager.getApplication().runWriteAction(action)
            showSuccess("Successfully generated $type for $featureName")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            showError("Error: ${e.message}")
            false
        }
    }
    
    private fun showSuccess(message: String) {
        Messages.showInfoMessage(project, message, "Success")
    }
    
    private fun showError(message: String) {
        Messages.showErrorDialog(project, message, "Error")
    }
}