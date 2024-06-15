package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.dialogs.plugin

import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.ErrorResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.Task
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.callback.CallbackTask
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.callback.Emitter
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.dialogs.Dialogs
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.SideEffectMediator

class DialogsSideEffectMediator : SideEffectMediator<DialogsSideEffectImpl>(), Dialogs {

    var retainedState = RetainedState()

    override fun show(dialogConfig: DialogConfig): Task<Boolean> = CallbackTask.create { emitter ->
        if (retainedState.record != null) {
            // на данный момент разрешено только 1 активное диалоговое окно за раз
            emitter.emit(ErrorResult(IllegalStateException("Can't launch more than 1 dialog at a time")))
            return@create
        }

        val wrappedEmitter = Emitter.wrap(emitter) {
            retainedState.record = null
        }

        val record = DialogRecord(wrappedEmitter, dialogConfig)
        wrappedEmitter.setCancelListener {
            target { implementation ->
                implementation.removeDialog()
            }
        }

        target { implementation ->
            implementation.showDialog(record)
        }

        retainedState.record = record
    }

    class DialogRecord(
        val emitter: Emitter<Boolean>,
        val config: DialogConfig
    )

    class RetainedState(
        var record: DialogRecord? = null
    )
}