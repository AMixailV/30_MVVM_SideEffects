package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.toasts

/**
 * Интерфейс для показа всплывающих сообщений пользователю из моделей представления.
 * Вам нужно добавить [ToastsPlugin] в свою активность, прежде чем использовать эту функцию.
 */
interface Toasts {

    /**
     * Показать простое всплывающее сообщение.
     */
    fun toast(message: String)

}