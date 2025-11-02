package com.alfayedoficial.fastcodegen.ui.panels

import com.alfayedoficial.fastcodegen.generator.RepoGenerator
import com.alfayedoficial.fastcodegen.ui.UIConstants
import java.awt.*
import javax.swing.*

/**
 * Panel for Full Feature (ViewModel + Repository) configuration
 */
class FeaturePanel(
    val featureNameField: JTextField = JTextField(),
    // ViewModel components
    val eventsCheckBox: JCheckBox = JCheckBox("Enable Events", true),
    val refreshCheckBox: JCheckBox = JCheckBox("Enable Refresh", true),
    val uiStateCheckBox: JCheckBox = JCheckBox("Enable UIState", true),
    val useCasesField: JTextField = JTextField(),
    // Repository components
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
        featureNameField.toolTipText = UIConstants.FEATURE_NAME_TOOLTIP
        eventsCheckBox.toolTipText = UIConstants.EVENTS_TOOLTIP
        refreshCheckBox.toolTipText = UIConstants.REFRESH_TOOLTIP
        uiStateCheckBox.toolTipText = UIConstants.UISTATE_TOOLTIP
        useCasesField.preferredSize = UIConstants.TEXT_FIELD_SIZE
        useCasesField.toolTipText = UIConstants.USE_CASES_TOOLTIP
        httpClientCheckBox.toolTipText = UIConstants.HTTP_CLIENT_TOOLTIP
        methodsPanel.layout = BoxLayout(methodsPanel, BoxLayout.Y_AXIS)
        
        // Add first method by default
        addMethodInput()
    }
    
    fun createPanel(): JPanel {
        val mainPanel = JPanel(BorderLayout())
        mainPanel.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)

        val scrollContent = JPanel()
        scrollContent.layout = BoxLayout(scrollContent, BoxLayout.Y_AXIS)

        // Feature Name
        val namePanel = PanelFactory.createNamePanel(UIConstants.FEATURE_NAME_LABEL, featureNameField)
        scrollContent.add(namePanel)
        scrollContent.add(Box.createVerticalStrut(UIConstants.LARGE_SPACING))

        // ViewModel Section
        val vmSection = PanelFactory.createBorderedPanel("ViewModel Configuration")
        eventsCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        refreshCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        uiStateCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        vmSection.add(eventsCheckBox)
        vmSection.add(refreshCheckBox)
        vmSection.add(uiStateCheckBox)
        vmSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))

        val useCasesLabel = JLabel(UIConstants.USE_CASES_LABEL + " (Optional)")
        useCasesLabel.alignmentX = JComponent.LEFT_ALIGNMENT
        vmSection.add(useCasesLabel)
        vmSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))
        useCasesField.preferredSize = Dimension(400, 25)
        useCasesField.alignmentX = JComponent.LEFT_ALIGNMENT
        vmSection.add(useCasesField)
        vmSection.alignmentX = JComponent.LEFT_ALIGNMENT
        scrollContent.add(vmSection)
        scrollContent.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))

        // Repository Section
        val repoSection = PanelFactory.createBorderedPanel("Repository Configuration")
        httpClientCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        repoSection.add(httpClientCheckBox)
        repoSection.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))

        val methodsLabel = JLabel(UIConstants.METHODS_LABEL + " (Optional)")
        methodsLabel.font = methodsLabel.font.deriveFont(Font.BOLD)
        methodsLabel.alignmentX = JComponent.LEFT_ALIGNMENT
        repoSection.add(methodsLabel)
        repoSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))

        val scrollPane = JScrollPane(methodsPanel)
        scrollPane.preferredSize = Dimension(450, 150)
        scrollPane.alignmentX = JComponent.LEFT_ALIGNMENT
        repoSection.add(scrollPane)
        repoSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))

        val addButton = JButton("+ Add Method")
        addButton.alignmentX = JComponent.LEFT_ALIGNMENT
        addButton.addActionListener {
            addMethodInput()
            scrollContent.revalidate()
            scrollContent.repaint()
        }
        repoSection.add(addButton)
        repoSection.alignmentX = JComponent.LEFT_ALIGNMENT

        scrollContent.add(repoSection)

        val mainScrollPane = JScrollPane(scrollContent)
        mainScrollPane.preferredSize = UIConstants.DIALOG_SIZE
        mainPanel.add(mainScrollPane, BorderLayout.CENTER)
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
    fun isEventsEnabled(): Boolean = eventsCheckBox.isSelected
    fun isRefreshEnabled(): Boolean = refreshCheckBox.isSelected
    fun isUiStateEnabled(): Boolean = uiStateCheckBox.isSelected
    fun getUseCases(): List<String> = useCasesField.text.split(",").map { it.trim() }.filter { it.isNotEmpty() }
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