package com.alfayedoficial.fastcodegen.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.PsiDirectory

/**
 * Utility functions for package name operations
 */
object PackageUtils {
    
    /**
     * Gets the package name from a PsiDirectory
     * @param project The current project
     * @param directory The directory to get package name from
     * @return Package name in dot notation (e.g., "com.example.feature")
     */
    fun getPackageName(project: Project, directory: PsiDirectory): String {
        // Try to get package from source root
        val sourceRoot = ProjectRootManager.getInstance(project)
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
        
        // Fallback: Find kotlin or java folder and extract path from there
        val sourcePath = directory.virtualFile.path
        val parts = sourcePath.split("/")
        val kotlinIndex = parts.indexOfLast { it == "kotlin" || it == "java" }
        
        return if (kotlinIndex >= 0 && kotlinIndex < parts.size - 1) {
            parts.drop(kotlinIndex + 1).joinToString(".")
        } else {
            ""
        }
    }
    
    /**
     * Creates or finds a subdirectory
     * @param parent Parent directory
     * @param name Subdirectory name
     * @return The subdirectory
     */
    fun findOrCreateSubdirectory(parent: PsiDirectory, name: String): PsiDirectory {
        return parent.findSubdirectory(name) ?: parent.createSubdirectory(name)
    }
}