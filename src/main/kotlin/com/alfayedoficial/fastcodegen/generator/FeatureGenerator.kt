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
        generateViewModel: Boolean = true,
        enableEvents: Boolean,
        enableRefresh: Boolean,
        enableUIState: Boolean,
        includeLoadMethod: Boolean,
        useCases: List<String>,
        // Repo config
        generateRepository: Boolean = true,
        methods: List<RepoGenerator.RepoMethod>,
        needsHttpClient: Boolean,
        // Screen config
        generateScreen: Boolean = false,
        hasNavigationBack: Boolean = true,
        navigationType: ScreenGenerator.NavigationType = ScreenGenerator.NavigationType.NONE,
        navParameters: List<ScreenGenerator.NavParameter> = emptyList()
    ) {
        println("=== Starting Full Feature generation ===")
        println("Feature: $featureName")

        // Generate Screen (optional)
        if (generateScreen) {
            println("Generating Screen...")
            val screenGenerator = ScreenGenerator(project, baseDirectory)
            screenGenerator.generate(
                featureName = featureName,
                hasNavigationBack = hasNavigationBack,
                navigationType = navigationType,
                navParameters = navParameters,
                injectViewModel = generateViewModel  // INJECT IF VIEWMODEL IS GENERATED
            )
        }

        // Generate ViewModel State
        if (generateViewModel) {
            println("Generating ViewModel State...")
            val viewModelGenerator = ViewModelStateGenerator(project, baseDirectory)
            viewModelGenerator.generate(featureName, enableEvents, enableRefresh, enableUIState, includeLoadMethod,useCases)
        }

        // Generate Repository
        if (generateRepository && methods.isNotEmpty()) {
            println("Generating Repository...")
            val repoGenerator = RepoGenerator(project, baseDirectory)
            repoGenerator.generate(featureName, methods, needsHttpClient)
        }

        println("=== Full Feature generation complete ===")
    }
}