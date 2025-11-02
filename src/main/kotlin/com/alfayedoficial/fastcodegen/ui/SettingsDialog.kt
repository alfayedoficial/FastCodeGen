package com.alfayedoficial.fastcodegen.ui

import com.alfayedoficial.fastcodegen.settings.FastCodeGenSettings
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiManager
import java.awt.*
import javax.swing.*

/**
 * Settings dialog for FastCodeGen plugin
 * Allows users to configure custom import paths
 */
class SettingsDialog(
    private val project: Project
) : DialogWrapper(project) {

    private val settings = FastCodeGenSettings.getInstance(project)

    // Text fields for all paths
    private val appViewModelField = JTextField(settings.appViewModelPath)
    private val viewModelConfigField = JTextField(settings.viewModelConfigPath)
    private val baseStateField = JTextField(settings.baseStatePath)
    private val baseEventField = JTextField(settings.baseEventPath)
    private val baseUIStateField = JTextField(settings.baseUIStatePath)
    private val refreshableField = JTextField(settings.refreshablePath)
    private val baseIntentField = JTextField(settings.baseIntentPath)
    private val koinModuleField = JTextField(settings.koinModulePath)

    init {
        title = "FastCodeGen Settings"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val mainPanel = JPanel(BorderLayout())
        mainPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        val scrollContent = JPanel()
        scrollContent.layout = BoxLayout(scrollContent, BoxLayout.Y_AXIS)

        // Info panel
        val infoPanel = JPanel(BorderLayout())
        infoPanel.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color(100, 150, 255)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        )
        val infoLabel = JLabel("<html><b>ℹ️ Configure Import Paths</b><br/>" +
                "Set the full package paths for your base classes.<br/>" +
                "Click 📁 to browse and auto-detect the package path.</html>")
        infoPanel.add(infoLabel, BorderLayout.CENTER)
        infoPanel.alignmentX = Component.LEFT_ALIGNMENT
        scrollContent.add(infoPanel)
        scrollContent.add(Box.createVerticalStrut(20))

        // ViewModel Section
        addSection(scrollContent, "ViewModel Base Classes (Required)")
        addPathFieldWithBrowse(scrollContent, "AppViewModel:", appViewModelField,
            "Full path to your AppViewModel base class")
        addPathFieldWithBrowse(scrollContent, "ViewModelConfig:", viewModelConfigField,
            "Full path to your ViewModelConfig class")

        scrollContent.add(Box.createVerticalStrut(15))

        // State Section
        addSection(scrollContent, "State Classes (Required)")
        addPathFieldWithBrowse(scrollContent, "BaseState:", baseStateField,
            "Full path to your BaseState interface/class")
        addPathFieldWithBrowse(scrollContent, "BaseEvent:", baseEventField,
            "Full path to your BaseEvent interface/class")
        addPathFieldWithBrowse(scrollContent, "BaseUIState:", baseUIStateField,
            "Full path to your BaseUIState interface/class")
        addPathFieldWithBrowse(scrollContent, "Refreshable:", refreshableField,
            "Full path to your Refreshable interface")
        addPathFieldWithBrowse(scrollContent, "BaseIntent:", baseIntentField,
            "Full path to your BaseIntent interface/class")

        scrollContent.add(Box.createVerticalStrut(15))

        // Koin Module Section
        addSection(scrollContent, "Dependency Injection (Optional)")
        addPathFieldWithBrowse(scrollContent, "Koin Module:", koinModuleField,
            "Full path to your Koin module (leave empty if not using Koin)")

        scrollContent.add(Box.createVerticalStrut(15))

        // Example panel
        val examplePanel = JPanel(BorderLayout())
        examplePanel.border = BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Example Paths"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        )
        val exampleLabel = JLabel("<html>" +
                "<b>Example:</b><br/>" +
                "• com.myapp.core.viewmodel.AppViewModel<br/>" +
                "• com.myapp.core.viewmodel.BaseState<br/>" +
                "• org.koin.core.module.Module (for Koin)" +
                "</html>")
        examplePanel.add(exampleLabel, BorderLayout.CENTER)
        examplePanel.alignmentX = Component.LEFT_ALIGNMENT
        scrollContent.add(examplePanel)

        val scrollPane = JScrollPane(scrollContent)
        scrollPane.preferredSize = Dimension(700, 550)
        scrollPane.border = null
        mainPanel.add(scrollPane, BorderLayout.CENTER)

        return mainPanel
    }

    private fun addSection(parent: JPanel, title: String) {
        val label = JLabel(title)
        label.font = label.font.deriveFont(Font.BOLD, 14f)
        label.alignmentX = Component.LEFT_ALIGNMENT
        parent.add(label)
        parent.add(Box.createVerticalStrut(8))
    }

    private fun addPathFieldWithBrowse(parent: JPanel, label: String, field: JTextField, tooltip: String) {
        val panel = JPanel(BorderLayout(10, 0))
        panel.alignmentX = Component.LEFT_ALIGNMENT
        panel.maximumSize = Dimension(Integer.MAX_VALUE, 30)

        val labelComponent = JLabel(label)
        labelComponent.preferredSize = Dimension(150, 25)
        panel.add(labelComponent, BorderLayout.WEST)

        field.toolTipText = tooltip
        panel.add(field, BorderLayout.CENTER)

        // Browse button with folder icon
        val browseButton = JButton("📁")
        browseButton.toolTipText = "Browse for class file"
        browseButton.preferredSize = Dimension(50, 25)
        browseButton.addActionListener {
            browseForClass(field)
        }
        panel.add(browseButton, BorderLayout.EAST)

        parent.add(panel)
        parent.add(Box.createVerticalStrut(5))
    }

    private fun browseForClass(field: JTextField) {
        val descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor()
            .withFileFilter { it.extension == "kt" || it.extension == "java" }
            .withTitle("Select Base Class File")
            .withDescription("Select the Kotlin or Java file containing the base class")

        val selectedFile = FileChooser.chooseFile(descriptor, project, null)
        if (selectedFile != null) {
            // Get the package path from the file
            val psiFile = PsiManager.getInstance(project).findFile(selectedFile)
            if (psiFile != null) {
                val packageName = getPackageFromFile(psiFile)
                val className = selectedFile.nameWithoutExtension
                val fullPath = if (packageName.isNotEmpty()) {
                    "$packageName.$className"
                } else {
                    className
                }
                field.text = fullPath
            }
        }
    }

    private fun getPackageFromFile(psiFile: com.intellij.psi.PsiFile): String {
        // For Kotlin files
        if (psiFile is org.jetbrains.kotlin.psi.KtFile) {
            return psiFile.packageFqName.asString()
        }

        // For Java files
        if (psiFile is com.intellij.psi.PsiJavaFile) {
            return psiFile.packageName
        }

        return ""
    }

    override fun doOKAction() {
        // Save settings
        settings.appViewModelPath = appViewModelField.text.trim()
        settings.viewModelConfigPath = viewModelConfigField.text.trim()
        settings.baseStatePath = baseStateField.text.trim()
        settings.baseEventPath = baseEventField.text.trim()
        settings.baseUIStatePath = baseUIStateField.text.trim()
        settings.refreshablePath = refreshableField.text.trim()
        settings.baseIntentPath = baseIntentField.text.trim()
        settings.koinModulePath = koinModuleField.text.trim()

        super.doOKAction()
    }
}