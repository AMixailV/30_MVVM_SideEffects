package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.dispatchers

import android.os.Handler
import android.os.Looper

/**
 * MainThreadDispatcher runs code blocks:
 * - if the current thread is Main Thread -> блок кода выполняется немедленно
 * - if the current thread is not Main Thread -> блок кода выполняется [Handler] in Main Thread
 */
class MainThreadDispatcher : Dispatcher {

    private val handler = Handler(Looper.getMainLooper())

    override fun dispatch(block: () -> Unit) {
        if (Looper.getMainLooper().thread.id == Thread.currentThread().id) {
            block()
        } else {
            handler.post(block)
        }
    }

}