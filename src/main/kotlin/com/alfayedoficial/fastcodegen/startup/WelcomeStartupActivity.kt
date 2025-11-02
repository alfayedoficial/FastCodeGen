package com.alfayedoficial.fastcodegen.startup

import com.alfayedoficial.fastcodegen.action.WelcomeDialog
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

/**
 * Shows welcome dialog on first plugin startup
 *
 * @author Ali Al-Shahat Ali
 */
class WelcomeStartupActivity : ProjectActivity {

    companion object {
        private const val WELCOME_SHOWN_KEY = "com.alfayedoficial.FastCodeGen.welcomeShown"
    }

    override suspend fun execute(project: Project) {
        val propertiesComponent = PropertiesComponent.getInstance()

        // Check if welcome dialog has been shown before
        if (!propertiesComponent.getBoolean(WELCOME_SHOWN_KEY, false)) {
            // Show welcome dialog on UI thread
            ApplicationManager.getApplication().invokeLater {
                WelcomeDialog(project).show()

                // Mark as shown
                propertiesComponent.setValue(WELCOME_SHOWN_KEY, true)
            }
        }
    }
}