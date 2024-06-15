package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.dialogs

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.Task
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.dialogs.plugin.DialogConfig


/**
Интерфейс побочных эффектов для управления диалогами из модели представления.
Перед использованием этой функции вам необходимо добавить [DialogsPlugin] в свою активность.
ПРЕДУПРЕЖДАТЬ! Обратите внимание, диалоги не сохраняются после закрытия приложения.
 */
interface Dialogs {

    /**
     * Показать диалоговое окно предупреждения пользователю и дождаться выбора пользователя.
     */
    fun show(dialogConfig: DialogConfig): Task<Boolean>

}