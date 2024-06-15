package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects

import android.content.Context

/**
 * Точка входа для каждого плагина побочных эффектов.
 */
interface SideEffectPlugin<Mediator, Implementation> {

    /**
     * Класс медиатора побочного действия.
     */
    val mediatorClass: Class<Mediator>

    /**
     * Создайте экземпляр посредника, который действует на стороне модели представления.
     */
    fun createMediator(applicationContext: Context): SideEffectMediator<Implementation>

    /**
     * Создайте реализацию для медиатора, созданного методом [createMediator]. Может возвращать `null`.
     * NULL-значение может быть полезно, если логика может быть реализована непосредственно
     * в посреднике (например, побочный эффект не требует экземпляра действия)
     */
    fun createImplementation(mediator: Mediator): Implementation? = null

}