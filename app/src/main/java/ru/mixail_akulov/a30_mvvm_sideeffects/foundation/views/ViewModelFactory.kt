package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views

import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.BaseApplication
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.BaseScreen.Companion.ARG_SCREEN
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.activity.ActivityDelegateHolder
import java.lang.reflect.Constructor

/**
 * Используйте этот метод для получения моделей представления из ваших фрагментов.
 */
inline fun <reified VM : ViewModel> BaseFragment.screenViewModel() = viewModels<VM> {
    val application = requireActivity().application as BaseApplication
    val screen = requireArguments().getSerializable(ARG_SCREEN) as BaseScreen

    val activityScopeViewModel = (requireActivity() as ActivityDelegateHolder).delegate.getActivityScopeViewModel()

    // формирование списка доступных зависимостей:
    // - singleton scope dependencies (repositories) -> from App class
    // - activity VM scope dependencies -> from ActivityScopeViewModel
    // - screen VM scope dependencies -> screen args
    val dependencies = listOf(screen) + activityScopeViewModel.sideEffectMediators + application.singletonScopeDependencies

    // creating factory
    ViewModelFactory(dependencies, this)
}

class ViewModelFactory(
    private val dependencies: List<Any>,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val constructors = modelClass.constructors
        val constructor = constructors.maxByOrNull { it.typeParameters.size }!!

        // - SavedStateHandle также является зависимостью от screen VM scope,
        // но получить его мы можем только здесь, поэтому объединяем его со списком других зависимостей:
        val dependenciesWithSavedState = dependencies + handle

        // создание списка аргументов для передачи в конструктор модели представления
        val arguments = findDependencies(constructor, dependenciesWithSavedState)

        // creating view-model
        return constructor.newInstance(*arguments.toTypedArray()) as T
    }

    private fun findDependencies(constructor: Constructor<*>, dependencies: List<Any>): List<Any> {
        val args = mutableListOf<Any>()
        // здесь мы перебираем аргументы конструктора модели представления и
        // для каждого аргумента ищем зависимость, которая может быть назначена аргументу
        constructor.parameterTypes.forEach { parameterClass ->
            val dependency = dependencies.first { parameterClass.isAssignableFrom(it.javaClass) }
            args.add(dependency)
        }
        return args
    }
}