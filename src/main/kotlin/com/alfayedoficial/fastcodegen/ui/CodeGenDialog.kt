package com.alfayedoficial.fastcodegen.ui

import com.alfayedoficial.fastcodegen.action.ViewDocumentationAction
import com.alfayedoficial.fastcodegen.action.WelcomeDialog
import com.alfayedoficial.fastcodegen.settings.FastCodeGenSettings
import com.alfayedoficial.fastcodegen.ui.panels.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import java.awt.*
import javax.swing.*

/**
 * Main dialog for FastCodeGen plugin
 * Refactored to be short and maintainable
 *
 * @author Ali Al-Shahat Ali
 */
class CodeGenDialog(
   private val project: Project,
   private val directory: PsiDirectory
) : DialogWrapper(project) {

   private val cardLayout = CardLayout()
   private val cardPanel = JPanel(cardLayout)
   private val nextButton = JButton("Next →")

   // Selection radio buttons
   private val screenRadio = JRadioButton("Screen")
   private val viewModelRadio = JRadioButton("ViewModel State")
   private val repoRadio = JRadioButton("Repository")
   private val featureRadio = JRadioButton("Full Feature")

   // Panel instances
   private val screenPanel = ScreenPanel(onBack = ::showSelection)
   private val viewModelPanel = ViewModelPanel(onBack = ::showSelection)
   private val repositoryPanel = RepositoryPanel(onBack = ::showSelection)
   private val featurePanel = FeaturePanel(onBack = ::showSelection)

   // Generation manager
   private val generationManager = GenerationManager(project, directory)

   init {
      title = "FastCodeGen"
      setOKButtonText("Generate")
      isOKActionEnabled = false
      init()
      setupListeners()
      validateSettings()
   }

   override fun createCenterPanel(): JComponent {
      cardPanel.add(createSelectionPanel(), "selection")
      cardPanel.add(screenPanel.createPanel(), "screen")
      cardPanel.add(viewModelPanel.createPanel(), "viewmodel")
      cardPanel.add(repositoryPanel.createPanel(), "repo")
      cardPanel.add(featurePanel.createPanel(), "feature")
      return cardPanel
   }

   override fun createNorthPanel(): JComponent {
      val panel = JPanel(BorderLayout())
      panel.border = BorderFactory.createEmptyBorder(10, 10, 5, 10)

      val settingsButton = JButton("⚙️")
      settingsButton.toolTipText = "Settings"
      settingsButton.preferredSize = Dimension(40, 30)
      settingsButton.addActionListener {
         val settingsDialog = SettingsDialog(project)
         if (settingsDialog.showAndGet()) {
            validateSettings()
         }
      }

      panel.add(settingsButton, BorderLayout.EAST)
      return panel
   }

   override fun createSouthPanel(): JComponent {
      val panel = JPanel(BorderLayout())

      // Documentation button
      val leftPanel = JPanel(FlowLayout(FlowLayout.LEFT))
      val docButton = JButton("📚 Help")
      docButton.toolTipText = "View documentation and examples"
      docButton.addActionListener { showDocumentationMenu(docButton) }
      leftPanel.add(docButton)
      panel.add(leftPanel, BorderLayout.WEST)

      // OK/Cancel buttons
      panel.add(super.createSouthPanel(), BorderLayout.EAST)
      return panel
   }

   private fun createSelectionPanel(): JPanel {
      return PanelFactory.createSelectionPanel(
         screenRadio,
         viewModelRadio,
         repoRadio,
         featureRadio,
         nextButton
      )
   }

   private fun validateSettings() {
      val settings = FastCodeGenSettings.getInstance(project)
      if (!settings.isValid()) {
         Messages.showErrorDialog(
            project,
            settings.getValidationError(),
            "FastCodeGen Settings Required"
         )
      }
   }

   private fun showDocumentationMenu(component: JComponent) {
      val popup = JPopupMenu()

      popup.add(createMenuItem("🚀 Welcome & Quick Start") {
         WelcomeDialog(project).show()
      })
      popup.addSeparator()
      popup.add(createMenuItem("📖 README - Getting Started") {
         ViewDocumentationAction.openDocumentation(project, "README.md")
      })
      popup.add(createMenuItem("📚 User Guide - Tutorials") {
         ViewDocumentationAction.openDocumentation(project, "USER_GUIDE.md")
      })
      popup.add(createMenuItem("⚡ Quick Reference") {
         ViewDocumentationAction.openDocumentation(project, "QUICK_REFERENCE.md")
      })
      popup.addSeparator()
      popup.add(createMenuItem("🔧 Implementation Steps") {
         ViewDocumentationAction.openDocumentation(project, "IMPLEMENTATION_STEPS.md")
      })
      popup.add(createMenuItem("📊 Plugin Summary") {
         ViewDocumentationAction.openDocumentation(project, "PLUGIN_SUMMARY.md")
      })
      popup.addSeparator()
      popup.add(createMenuItem("📑 Complete Index") {
         ViewDocumentationAction.openDocumentation(project, "INDEX.md")
      })

      popup.show(component, 0, component.height)
   }

   private fun createMenuItem(text: String, action: () -> Unit): JMenuItem {
      val item = JMenuItem(text)
      item.addActionListener { action() }
      return item
   }

   private fun setupListeners() {
      nextButton.addActionListener {
         when {
            screenRadio.isSelected -> showPanel("screen")
            viewModelRadio.isSelected -> showPanel("viewmodel")
            repoRadio.isSelected -> showPanel("repo")
            featureRadio.isSelected -> showPanel("feature")
         }
      }

      // Enable OK button when feature name is entered
      screenPanel.featureNameField.document.addDocumentListener(SimpleDocumentListener { checkOKEnabled() })
      viewModelPanel.featureNameField.document.addDocumentListener(SimpleDocumentListener { checkOKEnabled() })
      repositoryPanel.featureNameField.document.addDocumentListener(SimpleDocumentListener { checkOKEnabled() })
      featurePanel.featureNameField.document.addDocumentListener(SimpleDocumentListener { checkOKEnabled() })
   }

   private fun showPanel(name: String) {
      cardLayout.show(cardPanel, name)
      nextButton.isVisible = false
      checkOKEnabled()
   }

   private fun showSelection() {
      cardLayout.show(cardPanel, "selection")
      nextButton.isVisible = true
      isOKActionEnabled = false
   }

   private fun checkOKEnabled() {
      isOKActionEnabled = when {
         screenRadio.isSelected -> screenPanel.getFeatureName().isNotBlank()
         viewModelRadio.isSelected -> viewModelPanel.getFeatureName().isNotBlank()
         repoRadio.isSelected -> repositoryPanel.getFeatureName().isNotBlank()
         featureRadio.isSelected -> featurePanel.getFeatureName().isNotBlank()
         else -> false
      }
   }

   override fun doOKAction() {
      // Validate settings before generation
      val settings = FastCodeGenSettings.getInstance(project)
      if (!settings.isValid()) {
         Messages.showErrorDialog(project, settings.getValidationError(), "Settings Required")
         return
      }

      val success = when {
         screenRadio.isSelected -> { generationManager.generateScreen(
            screenPanel.getFeatureName(),
            screenPanel.hasNavigationBack(),
            screenPanel.getNavigationType(),
            screenPanel.getNavParameters()
            )
         }
         viewModelRadio.isSelected -> generationManager.generateViewModel(
            viewModelPanel.getFeatureName(),
            viewModelPanel.isEventsEnabled(),
            viewModelPanel.isRefreshEnabled(),
            viewModelPanel.isUiStateEnabled(),
            viewModelPanel.getUseCases()
         )
         repoRadio.isSelected -> generationManager.generateRepository(
            repositoryPanel.getFeatureName(),
            repositoryPanel.getMethods(),
            repositoryPanel.isHttpClientEnabled()
         )
         featureRadio.isSelected -> generationManager.generateFeature(
            featureName = featurePanel.getFeatureName(),
            generateViewModel = featurePanel.shouldGenerateViewModel(),
            enableEvents = featurePanel.isEventsEnabled(),
            enableRefresh = featurePanel.isRefreshEnabled(),
            enableUIState = featurePanel.isUiStateEnabled(),
            useCases = featurePanel.getUseCases(),
            generateRepository = featurePanel.shouldGenerateRepository(),
            methods = featurePanel.getMethods(),
            needsHttpClient = featurePanel.isHttpClientEnabled(),
            generateScreen = featurePanel.shouldGenerateScreen(),
            hasNavigationBack = featurePanel.hasNavigationBack(),
            navigationType = featurePanel.getNavigationType(),
            navParameters = featurePanel.getNavParameters()
         )
         else -> false
      }

      if (success) {
         super.doOKAction()
      }
   }

   private class SimpleDocumentListener(private val action: () -> Unit) : javax.swing.event.DocumentListener {
      override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = action()
      override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = action()
      override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = action()
   }
}