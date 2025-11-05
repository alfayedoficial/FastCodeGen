package com.alfayedoficial.fastcodegen.ui

import java.awt.Dimension

/**
 * UI Constants for consistent styling across the plugin
 */
object UIConstants {
    // Spacing
    const val SMALL_SPACING = 5
    const val MEDIUM_SPACING = 10
    const val LARGE_SPACING = 20

    // Sizes
    val TEXT_FIELD_SIZE = Dimension(300, 25)
    val SCROLL_PANE_SIZE = Dimension(450, 250)
    val DIALOG_SIZE = Dimension(550, 450)
    val PARAM_SCROLL_SIZE = Dimension(450, 100)
    val METHOD_SCROLL_SIZE = Dimension(450, 150)
    val SMALL_FIELD_SIZE = Dimension(15, 25)
    val REMOVE_BUTTON_SIZE = Dimension(45, 25)
    val REMOVE_BUTTON_LARGE_SIZE = Dimension(100, 25)

    // Main Labels
    const val FEATURE_NAME_LABEL = "Feature Name:"
    const val CONFIGURATION_LABEL = "Configuration:"
    const val USE_CASES_LABEL = "Use Cases (comma-separated)"
    const val METHODS_LABEL = "Repository Methods"
    const val NAVIGATION_TYPE_LABEL = "Navigation Type:"
    const val NAVIGATION_PARAMS_LABEL = "Navigation Parameters:"

    // Section Titles
    const val SCREEN_CONFIG_SECTION_TITLE = "Screen Configuration"
    const val NAVIGATION_CONFIG_SECTION_TITLE = "Navigation Configuration"
    const val SCREEN_SECTION_TITLE = "Screen Configuration"
    const val VIEWMODEL_SECTION_TITLE = "ViewModel Configuration"
    const val REPOSITORY_SECTION_TITLE = "Repository Configuration"
    const val GENERATION_OPTIONS_TITLE = "Generation Options"

    // Checkbox Labels
    const val GENERATE_SCREEN_LABEL = "Generate Screen"
    const val GENERATE_VIEWMODEL_LABEL = "Generate ViewModel"
    const val GENERATE_REPOSITORY_LABEL = "Generate Repository"
    const val HAS_NAVIGATION_BACK_LABEL = "Has Navigation Back"
    const val ENABLE_EVENTS_LABEL = "Enable Events"
    const val ENABLE_REFRESH_LABEL = "Enable Refresh"
    const val ENABLE_UISTATE_LABEL = "Enable UIState"
    const val NEEDS_HTTP_CLIENT_LABEL = "Needs HttpClient"

    // Navigation Types
    const val NAV_TYPE_NONE = "None"
    const val NAV_TYPE_SIMPLE = "Simple"
    const val NAV_TYPE_SAFE = "Type-Safe"

    // Button Labels
    const val ADD_PARAMETER_BUTTON = "+ Add Parameter"
    const val ADD_METHOD_BUTTON = "+ Add Method"
    const val REMOVE_BUTTON = "×"
    const val REMOVE_METHOD_BUTTON = "Remove"

    // Field Labels
    const val NAME_LABEL = "Name:"
    const val TYPE_LABEL = "Type:"
    const val METHOD_NAME_LABEL = "Method Name:"
    const val RETURN_TYPE_LABEL = "Return Type (Optional):"
    const val PARAMETERS_LABEL = "Parameters (Optional):"

    // Optional Labels
    const val OPTIONAL_SUFFIX = " (Optional)"

    // Method Panel Title Format
    const val METHOD_PANEL_TITLE_FORMAT = "Method %d"

    // Tooltips - Feature
    const val FEATURE_NAME_TOOLTIP = "Enter the name of your feature (e.g., Login, Home, Profile)"

    // Add to Tooltips section
    const val GENERATE_VIEWMODEL_TOOLTIP = "Generate ViewModel State files"
    const val GENERATE_REPOSITORY_TOOLTIP = "Generate Repository files"

    // Tooltips - Screen
    const val GENERATE_SCREEN_TOOLTIP = "Generate Compose Screen with navigation"
    const val NAVIGATION_BACK_TOOLTIP = "Add navigationBack parameter to screen"
    const val NAVIGATION_TYPE_TOOLTIP = "Choose navigation type: None, Simple (route constant), or Type-Safe (@Serializable)"
    const val NAV_PARAM_NAME_TOOLTIP = "Parameter name (e.g., userId, itemId)"
    const val NAV_PARAM_TYPE_TOOLTIP = "Parameter type (e.g., String, Int, Long)"

    // Tooltips - ViewModel
    const val EVENTS_TOOLTIP = "Enable events for one-time UI actions (navigation, toasts, etc.)"
    const val REFRESH_TOOLTIP = "Enable refresh for pull-to-refresh functionality"
    const val UISTATE_TOOLTIP = "Enable UIState for UI-specific data (form fields, loading states, etc.)"
    const val USE_CASES_TOOLTIP = "Enter use case names separated by commas (e.g., GetUser, UpdateProfile)"

    // Tooltips - Repository
    const val HTTP_CLIENT_TOOLTIP = "Include HttpClient injection for API calls"
    const val METHOD_NAME_TOOLTIP = "Method name in camelCase (e.g., getUsers, createOrder)"
    const val RETURN_TYPE_TOOLTIP = "Return type (can be empty for Unit)"
    const val PARAMS_TOOLTIP = "Parameters (can be empty for no params)"


}