package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.utils

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.dispatchers.Dispatcher

typealias ResourceAction<T> = (T) -> Unit

/**
 * Очередь действий, где действия выполняются только при наличии ресурса.
 * Если это не так, действие добавляется в очередь и ожидает, пока ресурс не станет доступным.
 */
class ResourceActions<T>(
    private val dispatcher: Dispatcher
) {

    var resource: T? = null
        set(newValue) {
            field = newValue
            if (newValue != null) {
                actions.forEach { action ->
                    dispatcher.dispatch {
                        action(newValue)
                    }
                }
                actions.clear()
            }
        }

    private val actions = mutableListOf<ResourceAction<T>>()

    /**
     * Вызывайте действие только тогда, когда [resource] существует (не нуль).
     * В противном случае действие откладывается до тех пор, пока [resource] не будет присвоено какое-либо ненулевое значение.
     */
    operator fun invoke(action: ResourceAction<T>) {
        val resource = this.resource
        if (resource == null) {
            actions += action
        } else {
            dispatcher.dispatch {
                action(resource)
            }
        }
    }

    fun clear() {
        actions.clear()
    }
}