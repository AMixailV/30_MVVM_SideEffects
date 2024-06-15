package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.dialogs.plugin

/**
 * Конфигурация диалогового окна предупреждения, отображаемого [Dialogs.show]
 */
data class DialogConfig(
    val title: String,
    val message: String,
    val positiveButton: String = "",
    val negativeButton: String = "",
    val cancellable: Boolean = true
)