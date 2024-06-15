package ru.mixail_akulov.a30_mvvm_sideeffects

import android.app.Application
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.BaseApplication
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.ThreadUtils
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.dispatchers.MainThreadDispatcher
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.factories.ExecutorServiceTasksFactory
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.factories.HandlerThreadTasksFactory
import ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.model.colors.InMemoryColorsRepository
import java.util.concurrent.Executors

/**
 * Здесь мы храним экземпляры классов слоя модели.
 */
class App : Application(), BaseApplication {

    // instances of all created task factories
    private val singleThreadExecutorTasksFactory = ExecutorServiceTasksFactory(Executors.newSingleThreadExecutor())
    private val handlerThreadTasksFactory = HandlerThreadTasksFactory()
    private val cachedThreadPoolExecutorTasksFactory = ExecutorServiceTasksFactory(Executors.newCachedThreadPool())

    private val threadUtils = ThreadUtils.Default()
    private val dispatcher = MainThreadDispatcher()


    /**
     * Place your singleton scope dependencies here
     */
    override val singletonScopeDependencies: List<Any> = listOf(
        cachedThreadPoolExecutorTasksFactory, // task factory to be used in view-models
        dispatcher, // dispatcher to be used in view-models

        InMemoryColorsRepository(cachedThreadPoolExecutorTasksFactory, threadUtils)
    )
}