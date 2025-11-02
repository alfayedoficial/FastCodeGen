package com.alfayedoficial.fastcodegen.ui.panels

import com.alfayedoficial.fastcodegen.ui.UIConstants
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.ButtonGroup
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JTextField

/**
 * Factory for creating dialog panels
 */
object PanelFactory {

    fun createSelectionPanel(
       viewModelRadio: JRadioButton,
       repoRadio: JRadioButton,
       featureRadio: JRadioButton,
       nextButton: JButton
    ): JPanel {
        val panel = JPanel(BorderLayout())
        panel.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)

        val centerPanel = JPanel()
        centerPanel.layout = BoxLayout(centerPanel, BoxLayout.Y_AXIS)

        // Title
        val titleLabel = JLabel("Select Generation Type")
        titleLabel.font = titleLabel.font.deriveFont(Font.BOLD, 16f)
        titleLabel.alignmentX = JComponent.LEFT_ALIGNMENT
        centerPanel.add(titleLabel)
        centerPanel.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))

        // Description
        val descLabel =
           JLabel("<html>Choose what you want to generate:<br/>Complete features, ViewModels, or Repositories</html>")
        descLabel.alignmentX = JComponent.LEFT_ALIGNMENT
        centerPanel.add(descLabel)
        centerPanel.add(Box.createVerticalStrut(UIConstants.LARGE_SPACING))

        // Radio buttons
        val buttonGroup = ButtonGroup()
        buttonGroup.add(viewModelRadio)
        buttonGroup.add(repoRadio)
        buttonGroup.add(featureRadio)

        viewModelRadio.toolTipText = "Generate ViewModel with State, Event, UIState, and Intent"
        repoRadio.toolTipText = "Generate Repository interface and implementation"
        featureRadio.toolTipText = "Generate complete feature with ViewModel and Repository"

        viewModelRadio.alignmentX = JComponent.LEFT_ALIGNMENT
        repoRadio.alignmentX = JComponent.LEFT_ALIGNMENT
        featureRadio.alignmentX = JComponent.LEFT_ALIGNMENT

        centerPanel.add(viewModelRadio)
        centerPanel.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))
        centerPanel.add(repoRadio)
        centerPanel.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))
        centerPanel.add(featureRadio)

        panel.add(centerPanel, BorderLayout.CENTER)

        // Next button
        val buttonPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        buttonPanel.add(nextButton)
        panel.add(buttonPanel, BorderLayout.SOUTH)

        return panel
    }

    fun createBackButton(onBack: () -> Unit): JPanel {
        val buttonPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val backButton = JButton("← Back")
        backButton.addActionListener { onBack() }
        buttonPanel.add(backButton)
        return buttonPanel
    }

    fun createBorderedPanel(title: String): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.border = BorderFactory.createTitledBorder(title)
        return panel
    }

    fun addSection(parent: JPanel, title: String, components: List<JComponent>) {
        val label = JLabel(title)
        label.font = label.font.deriveFont(Font.BOLD)
        label.alignmentX = JComponent.LEFT_ALIGNMENT
        parent.add(label)
        parent.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))
        components.forEach {
            it.alignmentX = JComponent.LEFT_ALIGNMENT
            parent.add(it)
        }
    }

    fun addLabeledComponent(parent: JPanel, label: String, component: JComponent) {
        val labelComp = JLabel(label)
        labelComp.alignmentX = JComponent.LEFT_ALIGNMENT
        parent.add(labelComp)
        parent.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))
        component.alignmentX = JComponent.LEFT_ALIGNMENT
        parent.add(component)
    }

    fun createNamePanel(label: String, textField: JTextField): JPanel {
        val namePanel = JPanel(FlowLayout(FlowLayout.LEFT, 5, 0))
        namePanel.add(JLabel(label))
        textField.preferredSize = Dimension(300, 25)
        namePanel.add(textField)
        namePanel.alignmentX = JComponent.LEFT_ALIGNMENT
        return namePanel
    }
}