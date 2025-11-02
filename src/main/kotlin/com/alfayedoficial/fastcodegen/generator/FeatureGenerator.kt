package com.alfayedoficial.fastcodegen.generator

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class FeatureGenerator(
    private val project: Project,
    private val baseDirectory: PsiDirectory
) {
    
    fun generate(
        featureName: String,
        // ViewModel config
        enableEvents: Boolean,
        enableRefresh: Boolean,
        enableUIState: Boolean,
        useCases: List<String>,
        // Repo config
        repoMethods: List<RepoGenerator.RepoMethod>,
        needsHttpClient: Boolean
    ) {
        println("=== Starting Full Feature generation ===")
        println("Feature: $featureName")
        
        // Generate ViewModel State
        println("Generating ViewModel State...")
        val viewModelGenerator = ViewModelStateGenerator(project, baseDirectory)
        viewModelGenerator.generate(featureName, enableEvents, enableRefresh, enableUIState, useCases)
        
        // Generate Repository
        if (repoMethods.isNotEmpty()) {
            println("Generating Repository...")
            val repoGenerator = RepoGenerator(project, baseDirectory)
            repoGenerator.generate(featureName, repoMethods, needsHttpClient)
        }
        
        println("=== Full Feature generation complete ===")
    }
}