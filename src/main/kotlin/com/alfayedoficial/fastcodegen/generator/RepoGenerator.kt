package com.alfayedoficial.fastcodegen.generator

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory

class RepoGenerator(
    private val project: Project,
    private val baseDirectory: PsiDirectory
) {
    
    data class RepoMethod(
        val name: String,
        val returnType: String,
        val parameters: String
    )
    
    fun generate(
        featureName: String,
        methods: List<RepoMethod>,
        needsHttpClient: Boolean
    ) {
        println("=== Starting Repo generation ===")
        println("Feature: $featureName")
        
        val featureClass = toPascalCase(featureName)
        println("Class: $featureClass")
        
        // Find domain and data folders
        val domainDir = findOrCreateFolder(baseDirectory, "domain")
        val dataDir = findOrCreateFolder(baseDirectory, "data")
        
        // Create repo folders
        val domainRepoDir = findOrCreateFolder(domainDir, "repo")
        val dataRepoDir = findOrCreateFolder(dataDir, "repo")
        
        println("Domain repo: ${domainRepoDir.virtualFile.path}")
        println("Data repo: ${dataRepoDir.virtualFile.path}")
        
        // Get packages
        val basePackage = getPackageName(baseDirectory)
        val domainRepoPackage = "$basePackage.domain.repo"
        val dataRepoPackage = "$basePackage.data.repo"
        
        println("Domain package: $domainRepoPackage")
        println("Data package: $dataRepoPackage")
        
        // Generate interface
        val interfaceContent = generateRepoInterface(featureClass, domainRepoPackage, methods)
        createKotlinFile(project, domainRepoDir, "${featureClass}Repo.kt", interfaceContent)
        println("Interface created")
        
        // Generate implementation
        val implContent = generateRepoImpl(featureClass, dataRepoPackage, domainRepoPackage, methods, needsHttpClient)
        createKotlinFile(project, dataRepoDir, "${featureClass}RepoImpl.kt", implContent)
        println("Implementation created")
        
        println("=== Repo generation complete ===")
    }
    
    private fun generateRepoInterface(
        featureClass: String,
        packageName: String,
        methods: List<RepoMethod>
    ): String {
        val sb = StringBuilder()
        
        sb.appendLine("package $packageName")
        sb.appendLine()
        sb.appendLine("import kotlinx.coroutines.flow.Flow")
        sb.appendLine()
        sb.appendLine("interface ${featureClass}Repo {")
        sb.appendLine()
        
        methods.forEach { method ->
            if (method.parameters.isNotEmpty()) {
                sb.appendLine("    fun ${method.name}(${method.parameters}): Flow<${method.returnType}>")
            } else {
                sb.appendLine("    fun ${method.name}(): Flow<${method.returnType}>")
            }
            sb.appendLine()
        }
        
        sb.appendLine("}")
        
        return sb.toString()
    }
    
    private fun generateRepoImpl(
        featureClass: String,
        packageName: String,
        domainPackage: String,
        methods: List<RepoMethod>,
        needsHttpClient: Boolean
    ): String {
        val sb = StringBuilder()
        
        sb.appendLine("package $packageName")
        sb.appendLine()
        sb.appendLine("import $domainPackage.${featureClass}Repo")
        if (needsHttpClient) {
            sb.appendLine("import io.ktor.client.HttpClient")
        }
        sb.appendLine("import kotlinx.coroutines.flow.Flow")
        sb.appendLine()
        
        if (needsHttpClient) {
            sb.appendLine("class ${featureClass}RepoImpl(")
            sb.appendLine("    private val httpClient: HttpClient,")
            sb.appendLine(") : ${featureClass}Repo {")
        } else {
            sb.appendLine("class ${featureClass}RepoImpl() : ${featureClass}Repo {")
        }
        sb.appendLine()
        
        methods.forEach { method ->
            if (method.parameters.isNotEmpty()) {
                sb.appendLine("    override fun ${method.name}(${method.parameters}): Flow<${method.returnType}> {")
            } else {
                sb.appendLine("    override fun ${method.name}(): Flow<${method.returnType}> {")
            }
            sb.appendLine("        TODO(\"Not yet implemented\")")
            sb.appendLine("    }")
            sb.appendLine()
        }
        
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
    
    private fun toPascalCase(input: String): String {
        val cleaned = input.replace(Regex("[^a-zA-Z0-9]"), "").lowercase()
        return cleaned.replaceFirstChar { it.uppercase() }
    }
}