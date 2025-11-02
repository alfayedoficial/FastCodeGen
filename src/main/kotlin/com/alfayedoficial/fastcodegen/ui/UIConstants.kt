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

    // Labels
    const val FEATURE_NAME_LABEL = "Feature Name:"
    const val CONFIGURATION_LABEL = "Configuration:"
    const val USE_CASES_LABEL = "Use Cases (comma-separated):"
    const val METHODS_LABEL = "Repository Methods:"

    // Tooltips
    const val FEATURE_NAME_TOOLTIP = "Enter the name of your feature (e.g., Login, Home, Profile)"
    const val EVENTS_TOOLTIP = "Enable events for one-time UI actions (navigation, toasts, etc.)"
    const val REFRESH_TOOLTIP = "Enable refresh for pull-to-refresh functionality"
    const val UISTATE_TOOLTIP = "Enable UIState for UI-specific data (form fields, loading states, etc.)"
    const val USE_CASES_TOOLTIP = "Enter use case names separated by commas (e.g., GetUser, UpdateProfile)"
    const val HTTP_CLIENT_TOOLTIP = "Include HttpClient injection for API calls"
    const val METHOD_NAME_TOOLTIP = "Method name in camelCase (e.g., getUsers, createOrder)"
    const val RETURN_TYPE_TOOLTIP = "Return type (e.g., User, List<Item>, Boolean)"
    const val PARAMS_TOOLTIP = "Parameters in format: name: Type (e.g., id: String, limit: Int)"
}