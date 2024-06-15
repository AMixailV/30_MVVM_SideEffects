package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.factories

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

/**
 * Фабрика, создающая задачи, которые запускаются указанным [ExecutorService].
 * Например, вы можете пройти [Executors.newCachedThreadPool] чтобы использовать пул кешированных потоков
 * or [Executors.newSingleThreadExecutor] для запуска задач по одной.
 */
class ExecutorServiceTasksFactory(
    private val executorService: ExecutorService
) : TasksFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ExecutorServiceTask(body))
    }

    private inner class ExecutorServiceTask<T>(
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var future: Future<*>? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            future = executorService.submit {
                executeBody(body, listener)
            }
        }

        override fun doCancel() {
            future?.cancel(true)
        }
    }
}