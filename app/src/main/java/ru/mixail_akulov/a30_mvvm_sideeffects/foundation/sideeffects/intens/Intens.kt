package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.intens

/**
Интерфейс побочных эффектов для запуска некоторых системных действий.
Перед использованием этой функции вам необходимо добавить [IntentsPlugin] в свою активность.
 */
interface Intents {

    /**
     * Откройте системные настройки для этого приложения.
     */
    fun openAppSettings()

}