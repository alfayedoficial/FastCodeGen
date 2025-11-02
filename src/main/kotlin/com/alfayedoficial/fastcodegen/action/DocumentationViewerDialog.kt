package com.alfayedoficial.fastcodegen.action

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JEditorPane
import javax.swing.JPanel

/**
 * Dialog to display documentation with markdown rendering
 *
 * @author Ali Al-Shahat Ali
 */
class DocumentationViewerDialog(
   private val project: Project?,
   private var title: String,
   private val content: String
) : DialogWrapper(project) {

   init {
      this.title = title
      init()
   }

   override fun createCenterPanel(): JComponent {
      // Convert markdown to HTML
      val htmlContent = markdownToHtml(content)

      // Create editor pane with HTML
      val editorPane = JEditorPane("text/html", htmlContent)
      editorPane.isEditable = false
      editorPane.caretPosition = 0

      // Wrap in scroll pane
      val scrollPane = JBScrollPane(editorPane)
      scrollPane.preferredSize = Dimension(800, 600)

      return scrollPane
   }

   override fun createActions() = arrayOf(okAction)

   private fun markdownToHtml(markdown: String): String {
      var html = markdown

      // Convert headers
      html = html.replace(Regex("^# (.+)$", RegexOption.MULTILINE), "<h1>$1</h1>")
      html = html.replace(Regex("^## (.+)$", RegexOption.MULTILINE), "<h2>$1</h2>")
      html = html.replace(Regex("^### (.+)$", RegexOption.MULTILINE), "<h3>$1</h3>")
      html = html.replace(Regex("^#### (.+)$", RegexOption.MULTILINE), "<h4>$1</h4>")

      // Convert bold and italic
      html = html.replace(Regex("\\*\\*(.+?)\\*\\*"), "<b>$1</b>")
      html = html.replace(Regex("\\*(.+?)\\*"), "<i>$1</i>")

      // Convert inline code
      html = html.replace(Regex("`(.+?)`"), "<code>$1</code>")

      // Convert code blocks
      html = html.replace(Regex("```[\\s\\S]*?```")) { matchResult ->
         val code = matchResult.value.removeSurrounding("```").trim()
         "<pre>$code</pre>"
      }

      // Convert links
      html = html.replace(Regex("\\[(.+?)\\]\\((.+?)\\)"), "<a href=\"$2\">$1</a>")

      // Convert unordered lists
      html = html.replace(Regex("^[*-] (.+)$", RegexOption.MULTILINE), "<li>$1</li>")

      // Wrap consecutive list items
      html = html.replace(Regex("(<li>.*?</li>\\s*)+")) { matchResult ->
         "<ul>${matchResult.value}</ul>"
      }

      // Convert horizontal rules
      html = html.replace(Regex("^(---|\\*\\*\\*)$", RegexOption.MULTILINE), "<hr>")

      // Convert paragraphs (double newline to paragraph break)
      html = html.replace(Regex("\\n\\n+"), "</p><p>")

      return """
            <html>
            <body style="font-family: Arial, sans-serif; padding: 20px; font-size: 14px;">
                <p>$html</p>
            </body>
            </html>
        """.trimIndent()
   }
}