package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.callback

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.FinalResult

typealias CancelListener = () -> Unit

typealias ExecutionListener<T> = (Emitter<T>) -> Unit

/**
 * Экземпляр эмиттера передается в [CallbackTask.create] в качестве аргумента,
 * поэтому вы можете использовать его для преобразования обратных вызовов в [Task].
 */
interface Emitter<T> { // Излучатель

    /**
     * Завершите связанную задачу с указанным результатом.
     */
    fun emit(finalResult: FinalResult<T>)

    /**
     * Назначьте дополнительный прослушиватель отмены.
     * Этот прослушиватель выполняется, когда связанная задача была отменена вызовом [Task.cancel].
     */
    fun setCancelListener(cancelListener: CancelListener)

    companion object {
        /**
         * Оберните эмиттер некоторым действием [onFinish], которое будет выполняться при
         * публикации результата или его отмене. Может быть полезно для логики очистки.
         */
        fun <T> wrap(emitter: Emitter<T>, onFinish: () -> Unit): Emitter<T> {
            return object : Emitter<T> {
                override fun emit(finalResult: FinalResult<T>) {
                    onFinish()
                    emitter.emit(finalResult)
                }

                override fun setCancelListener(cancelListener: CancelListener) {
                    emitter.setCancelListener {
                        onFinish()
                        cancelListener()
                    }
                }
            }
        }
    }
}