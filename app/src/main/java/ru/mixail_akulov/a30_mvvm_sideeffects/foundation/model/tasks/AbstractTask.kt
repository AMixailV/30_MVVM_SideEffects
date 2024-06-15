package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.ErrorResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.FinalResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.SuccessResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.dispatchers.Dispatcher
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.factories.TaskBody
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.utils.delegates.Await

/**
 * Базовый класс для упрощения создания новых задач.
 * Предоставляет 2 метода, которые должны быть реализованы: [doEnqueue] and [doCancel]
 */
abstract class AbstractTask<T> : Task<T> {

    private var finalResult by Await<FinalResult<T>>()

    final override fun await(): T {
        val wrapperListener: TaskListener<T> = {
            finalResult = it
        }
        doEnqueue(wrapperListener)
        try {
            when (val result = finalResult) {
                is ErrorResult -> throw result.exception
                is SuccessResult -> return result.data
            }
        } catch (e: Exception) {
            if (e is InterruptedException) {
                cancel()
                throw CancelledException(e)
            } else {
                throw e
            }
        }
    }

    final override fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>) {
        val wrappedListener: TaskListener<T> = {
            finalResult = it
            dispatcher.dispatch {
                listener(finalResult)
            }
        }
        doEnqueue(wrappedListener)
    }

    final override fun cancel() {
        finalResult = ErrorResult(CancelledException())
        doCancel()
    }

    fun executeBody(taskBody: TaskBody<T>, listener: TaskListener<T>) {
        try {
            val data = taskBody()
            listener(SuccessResult(data))
        } catch (e: Exception) {
            listener(ErrorResult(e))
        }
    }

    /**
     * Запустите задачу асинхронно. Слушатель должен быть вызван, когда задача завершена.
     * Вы также можете использовать [executeBody], если ваша задача выполняется [TaskBody] каким-то образом.
     */
    abstract fun doEnqueue(listener: TaskListener<T>)

    /**
     * Cancel the task.
     */
    abstract fun doCancel()

}