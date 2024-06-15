package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.navigator.plugin

import android.content.Context
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.navigator.Navigator
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.SideEffectMediator
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.SideEffectPlugin

/**
 * This plugin allows using [Navigator] interface in view-models.
 * This plugin may support different navigator implementations so you should pass the
 * desired navigator to the constructor. Now there is one default implementation: [StackFragmentNavigator].
 * Allows adding [Navigator] interface to view-model constructor.
 */
class NavigatorPlugin(
    private val navigator: Navigator,
) : SideEffectPlugin<Navigator, Navigator> {

    override val mediatorClass: Class<Navigator>
        get() = Navigator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Navigator> {
        return NavigatorSideEffectMediator()
    }

    override fun createImplementation(mediator: Navigator): Navigator {
        return navigator
    }

}