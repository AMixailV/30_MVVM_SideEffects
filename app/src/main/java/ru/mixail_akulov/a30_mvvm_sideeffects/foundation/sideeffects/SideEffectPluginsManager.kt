package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects

/**
 * Менеджер, который содержит все плагины побочных эффектов.
 */
class SideEffectPluginsManager {

    private val _plugins = mutableListOf<SideEffectPlugin<*, *>>()
    internal val plugins: List<SideEffectPlugin<*, *>>
        get() = _plugins

    /**
     * Зарегистрируйте новый плагин побочных эффектов.
     * Интерфейс посредника, указанный плагином, можно использовать в конструкторе моделей представления.
     */
    fun <Mediator, Implementation> register(plugin: SideEffectPlugin<Mediator, Implementation>) {
        _plugins.add(plugin)
    }
}