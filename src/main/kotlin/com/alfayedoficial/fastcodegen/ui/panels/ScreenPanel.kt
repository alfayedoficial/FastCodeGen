package com.alfayedoficial.fastcodegen.ui.panels

import com.alfayedoficial.fastcodegen.generator.ScreenGenerator
import com.alfayedoficial.fastcodegen.ui.UIConstants
import com.intellij.openapi.ui.ComboBox
import java.awt.BorderLayout
import java.awt.Font
import javax.swing.*

/**
 * Panel for Screen configuration
 */
class ScreenPanel(
   val featureNameField: JTextField = JTextField(),
   val navigationBackCheckBox: JCheckBox = JCheckBox(UIConstants.HAS_NAVIGATION_BACK_LABEL, true),
   val navigationTypeCombo: JComboBox<String> = ComboBox(arrayOf(
      UIConstants.NAV_TYPE_NONE,
      UIConstants.NAV_TYPE_SIMPLE,
      UIConstants.NAV_TYPE_SAFE
   )),
   private val onBack: () -> Unit
) {

   private val parametersPanel = JPanel()
   private val parametersList = mutableListOf<ParameterInput>()
   private val addButton = JButton(UIConstants.ADD_PARAMETER_BUTTON)
   private val paramsLabel = JLabel(UIConstants.NAVIGATION_PARAMS_LABEL)

   data class ParameterInput(
      val nameField: JTextField,
      val typeField: JTextField,
      val panel: JPanel
   )

   init {
      setupComponents()
   }

   private fun setupComponents() {
      featureNameField.preferredSize = UIConstants.TEXT_FIELD_SIZE
      featureNameField.toolTipText = UIConstants.FEATURE_NAME_TOOLTIP
      navigationBackCheckBox.toolTipText = UIConstants.NAVIGATION_BACK_TOOLTIP
      navigationTypeCombo.toolTipText = UIConstants.NAVIGATION_TYPE_TOOLTIP

      // Setup parameters panel
      parametersPanel.layout = BoxLayout(parametersPanel, BoxLayout.Y_AXIS)
      paramsLabel.font = paramsLabel.font.deriveFont(Font.BOLD)

      // Listen to navigation type changes
      navigationTypeCombo.addActionListener {
         updateParametersVisibility()
      }
   }

   fun createPanel(): JPanel {
      val mainPanel = JPanel(BorderLayout())
      mainPanel.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)

      val panel = JPanel()
      panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

      // Screen Configuration
      val screenSection = PanelFactory.createBorderedPanel(UIConstants.SCREEN_CONFIG_SECTION_TITLE)
      val namePanel = PanelFactory.createNamePanel(UIConstants.FEATURE_NAME_LABEL, featureNameField)
      screenSection.add(namePanel)
      screenSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))
      screenSection.add(navigationBackCheckBox)
      screenSection.alignmentX = JComponent.LEFT_ALIGNMENT
      panel.add(screenSection)
      panel.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))

      // Navigation Configuration
      val navSection = PanelFactory.createBorderedPanel(UIConstants.NAVIGATION_CONFIG_SECTION_TITLE)
      val navTypePanel = PanelFactory.createNamePanel(UIConstants.NAVIGATION_TYPE_LABEL, navigationTypeCombo)
      navSection.add(navTypePanel)
      navSection.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))

      // Parameters section
      paramsLabel.alignmentX = JComponent.LEFT_ALIGNMENT
      navSection.add(paramsLabel)
      navSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))

      val scrollPane = JScrollPane(parametersPanel)
      scrollPane.preferredSize = UIConstants.METHOD_SCROLL_SIZE
      scrollPane.alignmentX = JComponent.LEFT_ALIGNMENT
      navSection.add(scrollPane)
      navSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))

      addButton.addActionListener {
         addParameterInput()
         panel.revalidate()
         panel.repaint()
      }
      addButton.alignmentX = JComponent.LEFT_ALIGNMENT
      navSection.add(addButton)

      navSection.alignmentX = JComponent.LEFT_ALIGNMENT
      panel.add(navSection)

      // Initially hide parameters
      updateParametersVisibility()

      mainPanel.add(panel, BorderLayout.CENTER)
      mainPanel.add(PanelFactory.createBackButton(onBack), BorderLayout.SOUTH)

      return mainPanel
   }

   private fun updateParametersVisibility() {
      val isTypeSafe = navigationTypeCombo.selectedItem == UIConstants.NAV_TYPE_SAFE
      paramsLabel.isVisible = isTypeSafe
      parametersPanel.isVisible = isTypeSafe
      addButton.isVisible = isTypeSafe
   }

   private fun addParameterInput() {
      val paramPanel = JPanel()
      paramPanel.layout = BoxLayout(paramPanel, BoxLayout.X_AXIS)
      paramPanel.border = BorderFactory.createEmptyBorder(2, 0, 2, 0)
      paramPanel.alignmentX = JComponent.LEFT_ALIGNMENT

      val nameField = JTextField(15)
      nameField.toolTipText = UIConstants.NAV_PARAM_NAME_TOOLTIP

      val typeField = JTextField(15)
      typeField.toolTipText = UIConstants.NAV_PARAM_TYPE_TOOLTIP

      val removeButton = JButton(UIConstants.REMOVE_BUTTON)
      removeButton.preferredSize = UIConstants.REMOVE_BUTTON_SIZE

      paramPanel.add(JLabel(UIConstants.NAME_LABEL))
      paramPanel.add(Box.createHorizontalStrut(5))
      paramPanel.add(nameField)
      paramPanel.add(Box.createHorizontalStrut(10))
      paramPanel.add(JLabel(UIConstants.TYPE_LABEL))
      paramPanel.add(Box.createHorizontalStrut(5))
      paramPanel.add(typeField)
      paramPanel.add(Box.createHorizontalStrut(5))
      paramPanel.add(removeButton)

      val input = ParameterInput(nameField, typeField, paramPanel)
      parametersList.add(input)
      parametersPanel.add(paramPanel)

      removeButton.addActionListener {
         parametersList.remove(input)
         parametersPanel.remove(paramPanel)
         parametersPanel.revalidate()
         parametersPanel.repaint()
      }
   }

   fun getFeatureName(): String = featureNameField.text.trim()

   fun hasNavigationBack(): Boolean = navigationBackCheckBox.isSelected

   fun getNavigationType(): ScreenGenerator.NavigationType {
      return when (navigationTypeCombo.selectedItem as String) {
         UIConstants.NAV_TYPE_SIMPLE -> ScreenGenerator.NavigationType.SIMPLE
         UIConstants.NAV_TYPE_SAFE -> ScreenGenerator.NavigationType.TYPE_SAFE
         else -> ScreenGenerator.NavigationType.NONE
      }
   }

   fun getNavParameters(): List<ScreenGenerator.NavParameter> {
      return parametersList
         .filter { it.nameField.text.isNotBlank() && it.typeField.text.isNotBlank() }
         .map { ScreenGenerator.NavParameter(it.nameField.text.trim(), it.typeField.text.trim()) }
   }
}