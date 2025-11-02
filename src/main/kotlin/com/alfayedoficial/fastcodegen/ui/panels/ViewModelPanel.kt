package com.alfayedoficial.fastcodegen.ui.panels

import com.alfayedoficial.fastcodegen.ui.UIConstants
import java.awt.*
import javax.swing.*

/**
 * Panel for ViewModel State configuration
 */
class ViewModelPanel(
    val featureNameField: JTextField = JTextField(),
    val eventsCheckBox: JCheckBox = JCheckBox("Enable Events", true),
    val refreshCheckBox: JCheckBox = JCheckBox("Enable Refresh", true),
    val uiStateCheckBox: JCheckBox = JCheckBox("Enable UIState", true),
    val useCasesField: JTextField = JTextField(),
    private val onBack: () -> Unit
) {
    
    init {
        setupComponents()
    }
    
    private fun setupComponents() {
        featureNameField.preferredSize = UIConstants.TEXT_FIELD_SIZE
        featureNameField.toolTipText = UIConstants.FEATURE_NAME_TOOLTIP
        eventsCheckBox.toolTipText = UIConstants.EVENTS_TOOLTIP
        refreshCheckBox.toolTipText = UIConstants.REFRESH_TOOLTIP
        uiStateCheckBox.toolTipText = UIConstants.UISTATE_TOOLTIP
        useCasesField.preferredSize = UIConstants.TEXT_FIELD_SIZE
        useCasesField.toolTipText = UIConstants.USE_CASES_TOOLTIP
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

        // Configuration checkboxes
        PanelFactory.addSection(panel, UIConstants.CONFIGURATION_LABEL, listOf(
            eventsCheckBox,
            refreshCheckBox,
            uiStateCheckBox
        ))

        panel.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))

        // Use Cases
        val useCasesLabel = JLabel(UIConstants.USE_CASES_LABEL + " (Optional)")
        useCasesLabel.alignmentX = JComponent.LEFT_ALIGNMENT
        panel.add(useCasesLabel)
        panel.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))
        useCasesField.preferredSize = Dimension(400, 25)
        useCasesField.alignmentX = JComponent.LEFT_ALIGNMENT
        panel.add(useCasesField)

        mainPanel.add(panel, BorderLayout.CENTER)
        mainPanel.add(PanelFactory.createBackButton(onBack), BorderLayout.SOUTH)

        return mainPanel
    }
    
    fun getFeatureName(): String = featureNameField.text.trim()
    fun isEventsEnabled(): Boolean = eventsCheckBox.isSelected
    fun isRefreshEnabled(): Boolean = refreshCheckBox.isSelected
    fun isUiStateEnabled(): Boolean = uiStateCheckBox.isSelected
    fun getUseCases(): List<String> = useCasesField.text.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}