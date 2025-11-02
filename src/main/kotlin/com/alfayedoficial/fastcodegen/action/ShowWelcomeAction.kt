package com.alfayedoficial.fastcodegen.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import java.awt.Dimension
import javax.swing.*

/**
 * Welcome dialog for FastCodeGen plugin
 *
 * @author Ali Al-Shahat Ali
 */
class ShowWelcomeAction : AnAction() {

   override fun actionPerformed(e: AnActionEvent) {
      showWelcome(e.project)
   }

   /**
    * Show welcome dialog
    * @param project Current project (can be null)
    */
   fun showWelcome(project: com.intellij.openapi.project.Project?) {
      WelcomeDialog(project).show()
   }
}

class WelcomeDialog(private val project: com.intellij.openapi.project.Project?) : DialogWrapper(project) {

   init {
      title = "Welcome to FastCodeGen!"
      init()
   }

   override fun createCenterPanel(): JComponent {
      val panel = JPanel()
      panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)


      val htmlContent = """
            <html>
            <body style='width: 500px; font-family: Arial, sans-serif; padding: 20px;'>
                <h1 style='color: #4CAF50;'>🚀 Welcome to FastCodeGen v1.0.0!</h1>
                
                <p>Thank you for installing FastCodeGen! This plugin helps you generate Kotlin MVI boilerplate code quickly.</p>
                
                <h2>✨ Features</h2>
                <ul>
                    <li><strong>ViewModel State</strong> - Generate complete ViewModel with State, Event, UIState, Intent</li>
                    <li><strong>Repository</strong> - Generate Repository interface and implementation</li>
                    <li><strong>Full Feature</strong> - Generate complete feature with ViewModel + Repository</li>
                </ul>
                
                <h2>🎯 Quick Start</h2>
                <ol>
                    <li>Right-click on any package in your Kotlin project</li>
                    <li>Select <strong>New → FastCodeGen</strong></li>
                    <li>Choose generation type and configure options</li>
                    <li>Click Generate!</li>
                </ol>
                
                <h2>📚 Documentation</h2>
                <p>Access complete documentation from:</p>
                <ul>
                    <li><strong>Tools → FastCodeGen Documentation</strong></li>
                    <li>Or view files in project root: README.md, USER_GUIDE.md</li>
                </ul>
                
                <h2>🔗 Useful Links</h2>
                <ul>
                    <li><strong>README</strong> - Getting started guide</li>
                    <li><strong>User Guide</strong> - Detailed tutorials with examples</li>
                    <li><strong>Quick Reference</strong> - Fast lookup for options</li>
                    <li><strong>Implementation Steps</strong> - Setup and publishing guide</li>
                </ul>
                
                <h2>💡 Pro Tips</h2>
                <ul>
                    <li>Start with <strong>README.md</strong> for an overview</li>
                    <li>Check <strong>USER_GUIDE.md</strong> for step-by-step tutorials</li>
                    <li>Use <strong>Full Feature</strong> mode to generate everything at once</li>
                    <li>All fields have helpful tooltips - hover to see hints!</li>
                </ul>
                
                <h2>📞 Support</h2>
                <p>Need help? Contact me:</p>
                <ul>
                    <li><strong>Email:</strong> alialfayed.official@gmail.com</li>
                    <li><strong>LinkedIn:</strong> linkedin.com/in/alfayedoficial</li>
                </ul>
                
                <hr style='margin: 20px 0;'/>
                
                <p style='text-align: center; color: #666;'>
                    <em>Made with ❤️ by Ali Al-Shahat Ali</em><br/>
                    <strong>Happy Coding! 🎉</strong>
                </p>
            </body>
            </html>
        """.trimIndent()

      val editorPane = JEditorPane("text/html", htmlContent)
      editorPane.isEditable = false
      editorPane.caretPosition = 0

      val scrollPane = JBScrollPane(editorPane)
      scrollPane.preferredSize = Dimension(600, 500)

      panel.add(scrollPane)

      return panel
   }

   override fun createActions() = arrayOf(okAction)
}