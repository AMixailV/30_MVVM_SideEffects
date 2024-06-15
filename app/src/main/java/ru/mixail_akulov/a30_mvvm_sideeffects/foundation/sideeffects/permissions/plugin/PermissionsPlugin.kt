package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.permissions.plugin

import android.content.Context
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.SideEffectMediator
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.SideEffectPlugin

/**
 * Плагин для управления разрешениями от моделей просмотра.
 * Этот плагин позволяет использовать интерфейс [Permissions] для конструктора модели просмотра.
 */
class PermissionsPlugin :
    SideEffectPlugin<PermissionsSideEffectMediator, PermissionsSideEffectImpl> {

    override val mediatorClass: Class<PermissionsSideEffectMediator>
        get() = PermissionsSideEffectMediator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<PermissionsSideEffectImpl> {
        return PermissionsSideEffectMediator(applicationContext)
    }

    override fun createImplementation(mediator: PermissionsSideEffectMediator): PermissionsSideEffectImpl {
        return PermissionsSideEffectImpl(mediator.retainedState)
    }

}