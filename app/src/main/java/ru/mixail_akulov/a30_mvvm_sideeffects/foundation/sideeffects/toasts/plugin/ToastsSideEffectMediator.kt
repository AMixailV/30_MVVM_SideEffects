package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.toasts.plugin

import android.content.Context
import android.widget.Toast
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.dispatchers.MainThreadDispatcher
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.SideEffectMediator
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.toasts.Toasts

/**
 * Android-реализация [Toasts]. Отображение простого всплывающего сообщения и получение строки из ресурсов.
 */
class ToastsSideEffectMediator(
    private val appContext: Context
) : SideEffectMediator<Nothing>(), Toasts {

    private val dispatcher = MainThreadDispatcher()

    override fun toast(message: String) {
        dispatcher.dispatch {
            Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
        }
    }

}
