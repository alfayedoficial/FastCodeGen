package com.alfayedoficial.fastcodegen.ui.panels

import com.alfayedoficial.fastcodegen.generator.RepoGenerator
import com.alfayedoficial.fastcodegen.ui.UIConstants
import java.awt.*
import javax.swing.*

/**
 * Panel for Repository configuration
 */
class RepositoryPanel(
    val featureNameField: JTextField = JTextField(),
    val httpClientCheckBox: JCheckBox = JCheckBox("Needs HttpClient", true),
    private val onBack: () -> Unit
) {
    
    private val methodsPanel = JPanel()
    private val methodsList = mutableListOf<MethodInput>()
    
    data class MethodInput(
        val nameField: JTextField,
        val returnTypeField: JTextField,
        val paramsField: JTextField
    )
    
    init {
        setupComponents()
    }
    
    private fun setupComponents() {
        featureNameField.preferredSize = UIConstants.TEXT_FIELD_SIZE
        featureNameField.toolTipText = UIConstants.FEATURE_NAME_TOOLTIP
        httpClientCheckBox.toolTipText = UIConstants.HTTP_CLIENT_TOOLTIP
        methodsPanel.layout = BoxLayout(methodsPanel, BoxLayout.Y_AXIS)
        
        // Add first method by default
        addMethodInput()
    }
    
    fun createPanel(): JPanel {
        val mainPanel = JPanel(BorderLayout())
        mainPanel.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)

        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        // Feature Name
        val namePanel = PanelFactory.createNamePanel(UIConstants.FEATURE_NAME_LABEL, featureNameField)
        panel.add(namePanel)
        panel.add(Box.createVerticalStrut(UIConstants.LARGE_SPACING))

        // HTTP Client checkbox
        httpClientCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        panel.add(httpClientCheckBox)
        panel.add(Box.createVerticalStrut(UIConstants.LARGE_SPACING))

        // Methods section
        val methodsLabel = JLabel(UIConstants.METHODS_LABEL + " (Optional)")
        methodsLabel.font = methodsLabel.font.deriveFont(Font.BOLD)
        methodsLabel.alignmentX = JComponent.LEFT_ALIGNMENT
        panel.add(methodsLabel)
        panel.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))

        val scrollPane = JScrollPane(methodsPanel)
        scrollPane.preferredSize = Dimension(450, 200)
        scrollPane.alignmentX = JComponent.LEFT_ALIGNMENT
        panel.add(scrollPane)
        panel.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))

        // Add Method button
        val addButton = JButton("+ Add Method")
        addButton.alignmentX = JComponent.LEFT_ALIGNMENT
        addButton.addActionListener {
            addMethodInput()
            panel.revalidate()
            panel.repaint()
        }
        panel.add(addButton)

        mainPanel.add(panel, BorderLayout.CENTER)
        mainPanel.add(PanelFactory.createBackButton(onBack), BorderLayout.SOUTH)

        return mainPanel
    }
    
    private fun addMethodInput() {
        val methodPanel = JPanel()
        methodPanel.layout = BoxLayout(methodPanel, BoxLayout.Y_AXIS)
        methodPanel.border = BorderFactory.createTitledBorder("Method ${methodsList.size + 1}")

        val nameField = JTextField()
        val returnTypeField = JTextField()
        val paramsField = JTextField()

        nameField.toolTipText = UIConstants.METHOD_NAME_TOOLTIP
        returnTypeField.toolTipText = "Return type (can be empty for Unit)"
        paramsField.toolTipText = "Parameters (can be empty for no params)"

        PanelFactory.addLabeledComponent(methodPanel, "Method Name:", nameField)
        methodPanel.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))
        PanelFactory.addLabeledComponent(methodPanel, "Return Type (Optional):", returnTypeField)
        methodPanel.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))
        PanelFactory.addLabeledComponent(methodPanel, "Parameters (Optional):", paramsField)

        methodsPanel.add(methodPanel)
        methodsPanel.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))
        methodsList.add(MethodInput(nameField, returnTypeField, paramsField))

        methodsPanel.revalidate()
        methodsPanel.repaint()
    }
    
    fun getFeatureName(): String = featureNameField.text.trim()
    fun isHttpClientEnabled(): Boolean = httpClientCheckBox.isSelected
    
    fun getMethods(): List<RepoGenerator.RepoMethod> {
        return methodsList.mapNotNull { input ->
            val name = input.nameField.text.trim()
            val returnType = input.returnTypeField.text.trim().ifEmpty { "Unit" }
            val params = input.paramsField.text.trim()

            if (name.isNotEmpty()) {
                RepoGenerator.RepoMethod(name, returnType, params)
            } else {
                null
            }
        }
    }
}