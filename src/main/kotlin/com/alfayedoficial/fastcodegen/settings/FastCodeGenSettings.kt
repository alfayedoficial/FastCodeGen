package com.alfayedoficial.fastcodegen.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Settings for FastCodeGen plugin
 * Stores custom import paths for base classes
 */
@State(
    name = "FastCodeGenSettings",
    storages = [Storage("FastCodeGenSettings.xml")]
)
@Service(Service.Level.PROJECT)
class FastCodeGenSettings : PersistentStateComponent<FastCodeGenSettings> {

    // ViewModel related paths
    var appViewModelPath: String = "com.afapps.core.viewmodel.AppViewModel"
    var viewModelConfigPath: String = "com.afapps.core.viewmodel.ViewModelConfig"
    var baseStatePath: String = "com.afapps.core.viewmodel.BaseState"
    var baseEventPath: String = "com.afapps.core.viewmodel.BaseEvent"
    var baseUIStatePath: String = "com.afapps.core.viewmodel.BaseUIState"
    var refreshablePath: String = "com.afapps.core.viewmodel.Refreshable"
    var baseIntentPath: String = "com.afapps.core.viewmodel.BaseIntent"

    // Navigation utility paths
    var composableRoutePath: String = "com.afapps.core.utilities.composableRoute"
    var composableSafeTypePath: String = "com.afapps.core.utilities.composableSafeType"

    // Koin Module path (optional)
    var koinModulePath: String = ""

    override fun getState(): FastCodeGenSettings = this

    override fun loadState(state: FastCodeGenSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(project: Project): FastCodeGenSettings {
            return project.getService(FastCodeGenSettings::class.java)
        }
    }

    /**
     * Validates that all required paths are set
     */
    fun isValid(): Boolean {
        return appViewModelPath.isNotBlank() &&
                viewModelConfigPath.isNotBlank() &&
                baseStatePath.isNotBlank() &&
                baseEventPath.isNotBlank() &&
                baseUIStatePath.isNotBlank() &&
                refreshablePath.isNotBlank() &&
                baseIntentPath.isNotBlank() &&
                composableRoutePath.isNotBlank() &&
                composableSafeTypePath.isNotBlank()
    }

    /**
     * Gets validation error message if settings are invalid
     */
    fun getValidationError(): String {
        val missing = mutableListOf<String>()

        if (appViewModelPath.isBlank()) missing.add("AppViewModel path")
        if (viewModelConfigPath.isBlank()) missing.add("ViewModelConfig path")
        if (baseStatePath.isBlank()) missing.add("BaseState path")
        if (baseEventPath.isBlank()) missing.add("BaseEvent path")
        if (baseUIStatePath.isBlank()) missing.add("BaseUIState path")
        if (refreshablePath.isBlank()) missing.add("Refreshable path")
        if (baseIntentPath.isBlank()) missing.add("BaseIntent path")
        if (composableRoutePath.isBlank()) missing.add("composableRoute path")
        if (composableSafeTypePath.isBlank()) missing.add("composableSafeType path")

        return if (missing.isEmpty()) {
            ""
        } else {
            "Please configure the following paths in Settings:\n${missing.joinToString("\n• ", "• ")}"
        }
    }

    /**
     * Get the simple class name from full path
     */
    fun getClassName(fullPath: String): String {
        return fullPath.substringAfterLast('.')
    }
}