package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.factories

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.AbstractTask
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.SynchronizedTask
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.Task
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.TaskListener

/**
 * Фабрика, создающая задачи, которые запускаются в отдельном потоке:
 * один поток на каждую задачу. Темы создаются с помощью [Thread] class.
 */
class ThreadTasksFactory : TasksFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ThreadTask(body))
    }

    private class ThreadTask<T>(
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var thread: Thread? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            thread = Thread {
                executeBody(body, listener)
            }
            thread?.start()
        }

        override fun doCancel() {
            thread?.interrupt()
        }
    }
}