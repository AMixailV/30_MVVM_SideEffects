package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.FinalResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.dispatchers.Dispatcher

typealias TaskListener<T> = (FinalResult<T>) -> Unit

class CancelledException(
    originException: Exception? = null
) : Exception(originException)

/**
 * Base interface for all async operations.
 */
interface Task<T> {
    /**
     * Метод блокировки ожидания и получения результатов.
     * Выдает исключение в случае ошибки.
     * Задание может быть выполнено только один раз.
     * @throws [IllegalStateException] если задача уже выполнена
     * @throws [CancelledException] если задача была отменена
     */
    fun await(): T

    /**
     * Неблокирующий метод прослушивания результатов задания.
     * Если задача отменяется до завершения, прослушиватель не вызывается.
     * Если задача отменяется до вызова этого метода, задача не выполняется.
     * Задание может быть выполнено только один раз.
     *
     * listener вызывается через указанный диспетчер. обычно это [MainThreadDispatcher]
     * @throws [IllegalStateException] если задача уже выполнена.
     */
    fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>)

    /**
     * Отмените эту задачу и удалите прослушиватель, назначенный [enqueue].
     */
    fun cancel()
}