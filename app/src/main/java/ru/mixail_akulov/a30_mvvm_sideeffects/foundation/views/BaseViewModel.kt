package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.PendingResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.utils.Event
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.Result
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.Task
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.TaskListener
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.dispatchers.Dispatcher

// Альтернативные записи для сокращени кода
typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>
typealias LiveResult<T> = LiveData<Result<T>>

typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>

/**
 * Base class for all view-models.
 */

open class BaseViewModel(
    private val dispatcher: Dispatcher
) : ViewModel() {

    private val tasks = mutableSetOf<Task<*>>()

    override fun onCleared() {
        super.onCleared()
        clearTasks()
    }

    /**
     * Переопределите этот метод в дочерних классах, если вы хотите прослушивать результаты с других экранов.
     */
    open fun onResult(result: Any) {

    }

    /**
     * Переопределите этот метод в дочерних классах, если вы хотите контролировать поведение возврата.
     * Верните `true`, если вы хотите прервать закрытие этого экрана
     */
    open fun onBackPressed(): Boolean {
        clearTasks()
        return false
    }

    /**
     * запускать задачу асинхронно, прослушивать ее результат
     * и автоматически отписывать слушателя в случае разрушения модели представления.
     */
    fun <T> Task<T>.safeEnqueue(listener: TaskListener<T>? = null) {
        tasks.add(this)
            this.enqueue(dispatcher) {
                tasks.remove(this)
                listener?.invoke(it)
            }
    }

    /**
     * Запустить задачу асинхронно и сопоставить ее результат с указанным
     * [liveResult].
     * Задача автоматически отменяется, если модель представления будет уничтожена.
     */
    fun <T> Task<T>.into(liveResult: MutableLiveResult<T>) {
        liveResult.value = PendingResult()
        this.safeEnqueue {
            liveResult.value = it
        }
    }

    private fun clearTasks() {
        tasks.forEach { it.cancel() }
        tasks.clear()
    }

}

