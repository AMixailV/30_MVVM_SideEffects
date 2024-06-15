package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.callback

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.FinalResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.AbstractTask
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.SynchronizedTask
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.Task
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.TaskListener

/**
 * Преобразование обратных вызовов в [Задачу].
 * Например, если какая-то библиотека или Android SDK предоставляет обратный вызов для некоторых
 * операций (например, прослушиватели диалоговых кнопок, запрос местоположения и т. д.),
 * то эту задачу можно использовать для переноса обратных вызовов в интерфейс [Task].
 *
 * Пример использования:
 * ```
 * val task = CallbackTask.create { emitter ->
 *   val someNetworkCall: NetworkCall<User> = getUser("username")
 *
 *   emitter.setCancelListener { someNetworkCall.cancel() }
 *
 *   someNetworkCall.fetch(object : Callback<User> {
 *     override fun onSuccess(user: User) {
 *       emitter.emit(SuccessResult(user))
 *     }
 *
 *     override fun onError(error: Exception) {
 *       emitter.emit(ErrorResult(error))
 *     }
 *   })
 * }
 * ```
 *
 */

// Задача обратного вызова
class CallbackTask<T> private constructor(
    private val executionListener: ExecutionListener<T>
): AbstractTask<T>() {

    private var emitter: EmitterImpl<T>? = null

    override fun doEnqueue(listener: TaskListener<T>) {
        emitter = EmitterImpl(listener).also { executionListener(it) }
    }

    override fun doCancel() {
        emitter?.onCancelListener?.invoke()
    }

    companion object {
        fun <T> create(executionListener: ExecutionListener<T>): Task<T> {
            return SynchronizedTask(CallbackTask(executionListener))
        }
    }

    private class EmitterImpl<T>(
        private val taskListener: TaskListener<T>
    ) : Emitter<T> {
        var onCancelListener: CancelListener? = null

        override fun emit(finalResult: FinalResult<T>) {
            taskListener.invoke(finalResult)
        }

        override fun setCancelListener(cancelListener: CancelListener) {
            this.onCancelListener = cancelListener
        }
    }

}