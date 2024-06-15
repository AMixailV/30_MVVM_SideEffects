package ru.mixail_akulov.a30_mvvm_sideeffects.foundation

import androidx.lifecycle.ViewModel
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.SideEffectMediator
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.SideEffectMediatorsHolder

/**
 * Реализация [Navigator] и [UiActions].
 * Он основан на view-model активности, поскольку экземпляры [Navigator] и [UiActions] должны быть
 * доступны из view-model фрагментов (обычно они передаются конструктору view-model).
 * Держатель для медиаторов побочных эффектов.
 * Он основан на модели представления активности, потому что экземпляры посредников побочных эффектов
 * должны быть доступны из моделей представления фрагментов (обычно они передаются конструктору модели представления).
 */
class ActivityScopeViewModel : ViewModel() {

    internal val sideEffectMediatorsHolder = SideEffectMediatorsHolder()

    // contains the list of side-effect mediators that can be
    // passed to view-model constructors
    val sideEffectMediators: List<SideEffectMediator<*>>
        get() = sideEffectMediatorsHolder.mediators

    override fun onCleared() {
        super.onCleared()
        sideEffectMediatorsHolder.clear()
    }

}