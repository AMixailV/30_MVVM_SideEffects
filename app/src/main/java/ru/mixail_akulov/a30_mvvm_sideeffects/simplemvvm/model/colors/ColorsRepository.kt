package ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.model.colors

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.Repository
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.Task

typealias ColorListener = (NamedColor) -> Unit

/**
 * Пример интерфейса репозитория.
 *
 * Предоставляет доступ к доступным цветам и текущему выбранному цвету.
 */
interface ColorsRepository : Repository {

    /**
     * Получить список всех доступных цветов, которые может выбрать пользователь.
     */
    fun getAvailableColors(): Task<List<NamedColor>>

    /**
     * Get the color content by its ID
     */
    fun getById(id: Long): Task<NamedColor>

    /**
     * Get the current selected color.
     */
    fun getCurrentColor(): Task<NamedColor>

    /**
     * Set the specified color as current.
     */
    fun setCurrentColor(color: NamedColor): Task<Unit>

    /**
     * Слушайте текущие изменения цвета.
     * Слушатель запускается немедленно с текущим значением при вызове этого метода.
     */
    fun addListener(listener: ColorListener)

    /**
     * Перестаньте слушать текущие изменения цвета
     */
    fun removeListener(listener: ColorListener)
}