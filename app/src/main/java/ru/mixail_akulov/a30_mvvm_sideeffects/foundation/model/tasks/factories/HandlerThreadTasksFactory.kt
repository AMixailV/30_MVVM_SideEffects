package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.factories

import android.os.Handler
import android.os.HandlerThread
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.*

/**
 * Фабрика, создающая задачу, которая запускается только в 1 потоке, управляемом внутренним [HandlerThread].
 * На самом деле тела задач пока выполняются в отдельном потоке, но эти отдельные потоки управляются
 * [HandlerThread] поэтому одновременно активен только один поток, и задачи в любом случае запускаются одна за другой.
 */
class HandlerThreadTasksFactory : TasksFactory {

    private val thread = HandlerThread(javaClass.simpleName)

    init {
        thread.start()
    }

    private val handler = Handler(thread.looper)
    private var destroyed = false

    override fun <T> async(body: TaskBody<T>): Task<T> {
        if (destroyed) throw IllegalStateException("Factory is closed")
        return SynchronizedTask(HandlerThreadTask(body))
    }

    /**
     * Stop the [HandlerThread]. Весь дальнейший класс [async] будет генерировать исключение.
     */
    fun close() {
        destroyed = true
        thread.quitSafely()
    }

    private inner class HandlerThreadTask<T>(
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var thread: Thread? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            val runnable = Runnable {
                // использование потока для отмены задач, потому что Handler.removeCallbacks
                // не могу удалить задачи, которые уже запущены
                thread = Thread {
                    executeBody(body, listener)
                }
                thread?.start()
                // дождитесь завершения потока, иначе одновременно может быть выполнено более 1 тела задачи
                thread?.join()
            }
            handler.post(runnable)
        }

        override fun doCancel() {
            thread?.interrupt()
        }
    }
}