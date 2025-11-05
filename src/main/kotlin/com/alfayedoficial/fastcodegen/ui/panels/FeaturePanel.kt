package com.alfayedoficial.fastcodegen.ui.panels

import com.alfayedoficial.fastcodegen.generator.RepoGenerator
import com.alfayedoficial.fastcodegen.generator.ScreenGenerator
import com.alfayedoficial.fastcodegen.ui.UIConstants
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBScrollPane
import java.awt.*
import javax.swing.*

/**
 * Panel for Full Feature (Screen + ViewModel + Repository) configuration
 */
class FeaturePanel(
    val featureNameField: JTextField = JTextField(),
    // Screen components
    val navigationBackCheckBox: JCheckBox = JCheckBox(UIConstants.HAS_NAVIGATION_BACK_LABEL, true),
    val navigationTypeCombo: JComboBox<String> = ComboBox(arrayOf(
        UIConstants.NAV_TYPE_NONE,
        UIConstants.NAV_TYPE_SIMPLE,
        UIConstants.NAV_TYPE_SAFE
    )),
    // Feature flags
    val generateScreenCheckBox: JCheckBox = JCheckBox(UIConstants.GENERATE_SCREEN_LABEL, true),
    val generateViewModelCheckBox: JCheckBox = JCheckBox(UIConstants.GENERATE_VIEWMODEL_LABEL, true),
    val generateRepositoryCheckBox: JCheckBox = JCheckBox(UIConstants.GENERATE_REPOSITORY_LABEL, false),
    // ViewModel components
    val eventsCheckBox: JCheckBox = JCheckBox(UIConstants.ENABLE_EVENTS_LABEL, true),
    val refreshCheckBox: JCheckBox = JCheckBox(UIConstants.ENABLE_REFRESH_LABEL, true),
    val uiStateCheckBox: JCheckBox = JCheckBox(UIConstants.ENABLE_UISTATE_LABEL, true),
    val includeLoadMethodCheckBox: JCheckBox = JCheckBox(UIConstants.INCLUDE_LOAD_METHOD_LABEL, false),
    val useCasesField: JTextField = JTextField(),
    // Repository components
    val httpClientCheckBox: JCheckBox = JCheckBox(UIConstants.NEEDS_HTTP_CLIENT_LABEL, true),
    private val onBack: () -> Unit
) {

    private val screenSection = PanelFactory.createBorderedPanel(UIConstants.SCREEN_SECTION_TITLE)
    private val vmSection = PanelFactory.createBorderedPanel(UIConstants.VIEWMODEL_SECTION_TITLE)
    private val repoSection = PanelFactory.createBorderedPanel(UIConstants.REPOSITORY_SECTION_TITLE)

    // Private fields for Repository methods
    private val methodsPanel = JPanel()
    private val methodsList = mutableListOf<MethodInput>()

    // Private fields for Screen parameters
    private val screenParametersPanel = JPanel()
    private val screenParametersList = mutableListOf<ParameterInput>()
    private val addParamButton = JButton(UIConstants.ADD_PARAMETER_BUTTON)
    private val screenParamsLabel = JLabel(UIConstants.NAVIGATION_PARAMS_LABEL)
    private lateinit var screenScrollPane: JScrollPane

    data class MethodInput(
        val nameField: JTextField,
        val returnTypeField: JTextField,
        val paramsField: JTextField
    )

    data class ParameterInput(
        val nameField: JTextField,
        val typeField: JTextField,
        val panel: JPanel
    )

    init {
        setupComponents()
    }

    private fun setupComponents() {
        featureNameField.toolTipText = UIConstants.FEATURE_NAME_TOOLTIP

        // Screen tooltips
        generateScreenCheckBox.toolTipText = UIConstants.GENERATE_SCREEN_TOOLTIP
        navigationBackCheckBox.toolTipText = UIConstants.NAVIGATION_BACK_TOOLTIP
        navigationTypeCombo.toolTipText = UIConstants.NAVIGATION_TYPE_TOOLTIP
        screenParametersPanel.layout = BoxLayout(screenParametersPanel, BoxLayout.Y_AXIS)
        screenParamsLabel.font = screenParamsLabel.font.deriveFont(Font.BOLD)

        // Feature flags tooltips
        generateViewModelCheckBox.toolTipText = UIConstants.GENERATE_VIEWMODEL_TOOLTIP
        generateRepositoryCheckBox.toolTipText = UIConstants.GENERATE_REPOSITORY_TOOLTIP

        // ViewModel tooltips
        eventsCheckBox.toolTipText = UIConstants.EVENTS_TOOLTIP
        refreshCheckBox.toolTipText = UIConstants.REFRESH_TOOLTIP
        uiStateCheckBox.toolTipText = UIConstants.UISTATE_TOOLTIP
        includeLoadMethodCheckBox.toolTipText = UIConstants.INCLUDE_LOADING_METHOD_TOOLTIP
        useCasesField.preferredSize = UIConstants.TEXT_FIELD_SIZE
        useCasesField.toolTipText = UIConstants.USE_CASES_TOOLTIP

        // Repository tooltips
        httpClientCheckBox.toolTipText = UIConstants.HTTP_CLIENT_TOOLTIP
        methodsPanel.layout = BoxLayout(methodsPanel, BoxLayout.Y_AXIS)

        // Listen to checkboxes
        generateScreenCheckBox.addActionListener {
            updateScreenVisibility()
        }

        generateViewModelCheckBox.addActionListener {
            updateViewModelVisibility()
        }

        generateRepositoryCheckBox.addActionListener {
            updateRepositoryVisibility()
        }

        navigationTypeCombo.addActionListener {
            updateScreenParametersVisibility()
        }

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

        // Feature Flags Section
        val flagsSection = PanelFactory.createBorderedPanel(UIConstants.GENERATION_OPTIONS_TITLE)
        generateViewModelCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        generateRepositoryCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        flagsSection.add(generateScreenCheckBox)
        flagsSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))
        flagsSection.add(generateViewModelCheckBox)
        flagsSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))
        flagsSection.add(generateRepositoryCheckBox)
        flagsSection.alignmentX = JComponent.LEFT_ALIGNMENT
        scrollContent.add(flagsSection)
        scrollContent.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))

        // Screen Section
        navigationBackCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        screenSection.add(navigationBackCheckBox)
        screenSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))

        val navTypePanel = PanelFactory.createNamePanel(UIConstants.NAVIGATION_TYPE_LABEL, navigationTypeCombo)
        navTypePanel.alignmentX = JComponent.LEFT_ALIGNMENT
        screenSection.add(navTypePanel)
        screenSection.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))

        // Screen Parameters
        screenParamsLabel.alignmentX = JComponent.LEFT_ALIGNMENT
        screenSection.add(screenParamsLabel)
        screenSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))

        screenScrollPane = JBScrollPane(screenParametersPanel)  // INITIALIZE HERE
        screenScrollPane.preferredSize = UIConstants.PARAM_SCROLL_SIZE
        screenScrollPane.alignmentX = JComponent.LEFT_ALIGNMENT
        screenSection.add(screenScrollPane)
        screenSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))

        addParamButton.addActionListener {
            addScreenParameterInput()
            scrollContent.revalidate()
            scrollContent.repaint()
        }
        addParamButton.alignmentX = JComponent.LEFT_ALIGNMENT
        screenSection.add(addParamButton)

        screenSection.alignmentX = JComponent.LEFT_ALIGNMENT
        scrollContent.add(screenSection)
        scrollContent.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))

        // ViewModel Section
        eventsCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        refreshCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        uiStateCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        includeLoadMethodCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        vmSection.add(eventsCheckBox)
        vmSection.add(refreshCheckBox)
        vmSection.add(uiStateCheckBox)
        vmSection.add(includeLoadMethodCheckBox)
        vmSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))

        val useCasesLabel = JLabel(UIConstants.USE_CASES_LABEL + UIConstants.OPTIONAL_SUFFIX)
        useCasesLabel.alignmentX = JComponent.LEFT_ALIGNMENT
        vmSection.add(useCasesLabel)
        vmSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))
        useCasesField.preferredSize = Dimension(400, 35)
        useCasesField.alignmentX = JComponent.LEFT_ALIGNMENT
        vmSection.add(useCasesField)
        vmSection.alignmentX = JComponent.LEFT_ALIGNMENT
        scrollContent.add(vmSection)
        scrollContent.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))

        // Repository Section
        httpClientCheckBox.alignmentX = JComponent.LEFT_ALIGNMENT
        repoSection.add(httpClientCheckBox)
        repoSection.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))

        val methodsLabel = JLabel(UIConstants.METHODS_LABEL + UIConstants.OPTIONAL_SUFFIX)
        methodsLabel.font = methodsLabel.font.deriveFont(Font.BOLD)
        methodsLabel.alignmentX = JComponent.LEFT_ALIGNMENT
        repoSection.add(methodsLabel)
        repoSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))

        val scrollPane = JScrollPane(methodsPanel)
        scrollPane.preferredSize = UIConstants.METHOD_SCROLL_SIZE
        scrollPane.alignmentX = JComponent.LEFT_ALIGNMENT
        repoSection.add(scrollPane)
        repoSection.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))

        val addButton = JButton(UIConstants.ADD_METHOD_BUTTON)
        addButton.alignmentX = JComponent.LEFT_ALIGNMENT
        addButton.addActionListener {
            addMethodInput()
            scrollContent.revalidate()
            scrollContent.repaint()
        }
        repoSection.add(addButton)
        repoSection.alignmentX = JComponent.LEFT_ALIGNMENT

        scrollContent.add(repoSection)

        // Initially update visibility
        updateScreenVisibility()
        updateViewModelVisibility()
        updateRepositoryVisibility()
        updateScreenParametersVisibility()

        val mainScrollPane = JScrollPane(scrollContent)
        mainScrollPane.preferredSize = UIConstants.DIALOG_SIZE
        mainPanel.add(mainScrollPane, BorderLayout.CENTER)
        mainPanel.add(PanelFactory.createBackButton(onBack), BorderLayout.SOUTH)

        return mainPanel
    }

    private fun updateScreenVisibility() {
        val isEnabled = generateScreenCheckBox.isSelected
        screenSection.isVisible = isEnabled
        updateScreenParametersVisibility()
    }

    private fun updateViewModelVisibility() {
        val isEnabled = generateViewModelCheckBox.isSelected
        vmSection.isVisible = isEnabled
    }

    private fun updateRepositoryVisibility() {
        val isEnabled = generateRepositoryCheckBox.isSelected
        repoSection.isVisible = isEnabled
    }

    private fun updateScreenParametersVisibility() {
        val isTypeSafe = navigationTypeCombo.selectedItem == UIConstants.NAV_TYPE_SAFE && generateScreenCheckBox.isSelected
        screenParamsLabel.isVisible = isTypeSafe
        screenScrollPane.isVisible = isTypeSafe
        screenParametersPanel.isVisible = isTypeSafe
        addParamButton.isVisible = isTypeSafe
    }

    private fun addScreenParameterInput() {
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
        screenParametersList.add(input)
        screenParametersPanel.add(paramPanel)

        removeButton.addActionListener {
            screenParametersList.remove(input)
            screenParametersPanel.remove(paramPanel)
            screenParametersPanel.revalidate()
            screenParametersPanel.repaint()
        }
    }

    private fun addMethodInput() {
        val methodPanel = JPanel()
        methodPanel.layout = BoxLayout(methodPanel, BoxLayout.Y_AXIS)
        methodPanel.border = BorderFactory.createTitledBorder(
            UIConstants.METHOD_PANEL_TITLE_FORMAT.format(methodsList.size + 1)
        )

        val nameField = JTextField()
        val returnTypeField = JTextField()
        val paramsField = JTextField()

        nameField.toolTipText = UIConstants.METHOD_NAME_TOOLTIP
        returnTypeField.toolTipText = UIConstants.RETURN_TYPE_TOOLTIP
        paramsField.toolTipText = UIConstants.PARAMS_TOOLTIP

        PanelFactory.addLabeledComponent(methodPanel, UIConstants.METHOD_NAME_LABEL, nameField)
        methodPanel.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))
        PanelFactory.addLabeledComponent(methodPanel, UIConstants.RETURN_TYPE_LABEL, returnTypeField)
        methodPanel.add(Box.createVerticalStrut(UIConstants.SMALL_SPACING))
        PanelFactory.addLabeledComponent(methodPanel, UIConstants.PARAMETERS_LABEL, paramsField)

        methodsPanel.add(methodPanel)
        methodsPanel.add(Box.createVerticalStrut(UIConstants.MEDIUM_SPACING))
        methodsList.add(MethodInput(nameField, returnTypeField, paramsField))

        methodsPanel.revalidate()
        methodsPanel.repaint()
    }

    // Feature Name
    fun getFeatureName(): String = featureNameField.text.trim()

    // Feature Flags
    fun shouldGenerateViewModel(): Boolean = generateViewModelCheckBox.isSelected
    fun shouldGenerateRepository(): Boolean = generateRepositoryCheckBox.isSelected

    // Screen Configuration
    fun shouldGenerateScreen(): Boolean = generateScreenCheckBox.isSelected
    fun hasNavigationBack(): Boolean = navigationBackCheckBox.isSelected
    fun getNavigationType(): ScreenGenerator.NavigationType {
        return when (navigationTypeCombo.selectedItem as String) {
            UIConstants.NAV_TYPE_SIMPLE -> ScreenGenerator.NavigationType.SIMPLE
            UIConstants.NAV_TYPE_SAFE -> ScreenGenerator.NavigationType.TYPE_SAFE
            else -> ScreenGenerator.NavigationType.NONE
        }
    }
    fun getNavParameters(): List<ScreenGenerator.NavParameter> {
        return screenParametersList
            .filter { it.nameField.text.isNotBlank() && it.typeField.text.isNotBlank() }
            .map { ScreenGenerator.NavParameter(it.nameField.text.trim(), it.typeField.text.trim()) }
    }

    // ViewModel Configuration
    fun isEventsEnabled(): Boolean = eventsCheckBox.isSelected
    fun isRefreshEnabled(): Boolean = refreshCheckBox.isSelected
    fun isUiStateEnabled(): Boolean = uiStateCheckBox.isSelected
    fun isIncludeLoadMethod(): Boolean = includeLoadMethodCheckBox.isSelected
    fun getUseCases(): List<String> = useCasesField.text.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    // Repository Configuration
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