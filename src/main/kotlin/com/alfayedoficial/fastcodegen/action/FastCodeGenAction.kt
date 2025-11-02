package com.alfayedoficial.fastcodegen.action

import com.alfayedoficial.fastcodegen.ui.CodeGenDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager

class FastCodeGenAction : AnAction() {

   override fun actionPerformed(e: AnActionEvent) {
      val project = e.project ?: return
      val directory = getTargetDirectory(e) ?: return

      CodeGenDialog(project, directory).show()
   }

   override fun update(e: AnActionEvent) {
      val project = e.project
      val directory = getTargetDirectory(e)

      // Enable if we have a project and can find a directory
      e.presentation.isEnabledAndVisible = project != null && directory != null
   }

   // CRITICAL: This method is required for IntelliJ IDEA 2024.2+
   override fun getActionUpdateThread(): ActionUpdateThread {
      // Use BGT (Background Thread) for better performance
      // EDT (Event Dispatch Thread) can also be used if you need UI access in update()
      return ActionUpdateThread.BGT
   }

   private fun getTargetDirectory(e: AnActionEvent): PsiDirectory? {
      // 1. Try IdeView first (this is used by New menu)
      val view = e.getData(LangDataKeys.IDE_VIEW)
      view?.directories?.firstOrNull()?.let {
         return it
      }

      // 2. Direct PSI directory
      val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT)
      if (psiElement is PsiDirectory) {
         return psiElement
      }

      // 3. From PSI file's containing directory
      val psiFile = e.getData(CommonDataKeys.PSI_FILE)
      psiFile?.containingDirectory?.let {
         return it
      }

      // 4. From virtual file
      val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
      val project = e.project
      if (virtualFile != null && project != null) {
         val psiManager = PsiManager.getInstance(project)
         if (virtualFile.isDirectory) {
            return psiManager.findDirectory(virtualFile)
         } else {
            virtualFile.parent?.let { parentDir ->
               return psiManager.findDirectory(parentDir)
            }
         }
      }

      return null
   }
}