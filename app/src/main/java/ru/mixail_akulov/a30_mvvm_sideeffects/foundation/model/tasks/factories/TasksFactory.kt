package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.factories

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.Task

typealias TaskBody<T> = () -> T

/**
 * Фабрика для создания экземпляров асинхронных задач ([Task]) из синхронного кода, определенного [TaskBody]
 */
interface TasksFactory {

    /**
     * Create a new [Task] экземпляр из указанного тела.
     */
    fun <T> async(body: TaskBody<T>): Task<T>
}
