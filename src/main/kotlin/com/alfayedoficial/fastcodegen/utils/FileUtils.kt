package com.alfayedoficial.fastcodegen.utils

import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory

/**
 * Utility functions for file operations
 */
object FileUtils {
    
    /**
     * Creates a Kotlin file in the specified directory
     * @param project The current project
     * @param directory The directory to create file in
     * @param fileName The name of the file (must end with .kt)
     * @param content The content of the file
     * @throws IllegalArgumentException if file name doesn't end with .kt
     */
    fun createKotlinFile(
        project: Project,
        directory: PsiDirectory,
        fileName: String,
        content: String
    ) {
        require(fileName.endsWith(".kt")) { "File name must end with .kt" }
        
        // Delete existing file if present
        directory.findFile(fileName)?.delete()
        
        // Create new file
        val fileType = FileTypeManager.getInstance().getFileTypeByExtension("kt")
        val psiFile = PsiFileFactory.getInstance(project).createFileFromText(
            fileName,
            fileType,
            content
        )
        
        directory.add(psiFile)
    }
    
    /**
     * Checks if a file exists in directory
     * @param directory The directory to check
     * @param fileName The file name
     * @return true if file exists
     */
    fun fileExists(directory: PsiDirectory, fileName: String): Boolean {
        return directory.findFile(fileName) != null
    }
}