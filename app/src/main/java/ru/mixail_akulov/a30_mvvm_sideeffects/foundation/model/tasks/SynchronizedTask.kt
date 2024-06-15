package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.dispatchers.Dispatcher
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Wrapper class for other task.
 * Содержит общую логику синхронизации.
 * Гарантирует, что методы упакованной задачи выполняются в правильном порядке.
 * Не допускает угловых случаев, например:
 * - запуск задачи более 1 раза,
 * - запуск слушателей более 1 раза,
 * - запуск задачи после отмены,
 * - отмена уже завершенной задачи и так далее.
 */
class SynchronizedTask<T>(
    private val task: Task<T>
) : Task<T> {

    @Volatile
    private var cancelled = false

    private var executed = false

    private var listenerCalled = AtomicBoolean(false)

    override fun await(): T {
        synchronized(this) {
            if (cancelled) throw CancelledException()
            if (executed) throw IllegalStateException("Task has been executed")
            executed = true
        }
        // await находится вне синхронизированного блока, чтобы разрешить отмену из другого потока
        return task.await()
    }

    override fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>) = synchronized(this) {
        if (cancelled) return
        if (executed) throw IllegalStateException("Task has been executed")
        executed = true

        val finalListener: TaskListener<T> = { result ->
            // этот блок кода не синхронизирован, так как он запускается после завершения метода enqueue()
            if (listenerCalled.compareAndSet(false, true)) {
                if (!cancelled) listener(result)
            }
        }

        task.enqueue(dispatcher, finalListener)
    }

    override fun cancel() = synchronized(this) {
        if (listenerCalled.compareAndSet(false, true)) {
            if (cancelled) return
            cancelled = true
            task.cancel()
        }
    }

}