package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.dispatchers.Dispatcher
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.dispatchers.MainThreadDispatcher
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.utils.ResourceActions

/**
 * Базовый класс для всех медиаторов побочных эффектов.
 * Эти посредники живут в [ActivityScopeViewModel].
 * Посредник должен делегировать всю логику, связанную с пользовательским интерфейсом, реализациям через поле [target].
 */
open class SideEffectMediator<Implementation>(
    dispatcher: Dispatcher = MainThreadDispatcher()
) {

    protected val target = ResourceActions<Implementation>(dispatcher)

    /**
     * Назначить/Отменить назначение целевой реализации для этого поставщика.
     */
    fun setTarget(target: Implementation?) {
        this.target.resource = target
    }

    fun clear() {
        target.clear()
    }
}