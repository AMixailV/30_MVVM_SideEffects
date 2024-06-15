package ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.views.currentcolor

import android.Manifest
import ru.mixail_akulov.a30_mvvm_sideeffects.R
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.PendingResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.SuccessResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.takeSuccess
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.dispatchers.Dispatcher
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks.factories.TasksFactory
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.dialogs.Dialogs
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.dialogs.plugin.DialogConfig
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.intens.Intents
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.navigator.Navigator
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.permissions.Permissions
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.permissions.plugin.PermissionStatus
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.resources.Resources
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects.toasts.Toasts
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.BaseViewModel
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.LiveResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.MutableLiveResult
import ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.model.colors.ColorListener
import ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.model.colors.ColorsRepository
import ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.model.colors.NamedColor
import ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.views.changecolor.ChangeColorFragment

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val permissions: Permissions,
    private val intents: Intents,
    private val dialogs: Dialogs,
    private val tasksFactory: TasksFactory,
    private val colorsRepository: ColorsRepository,
    dispatcher: Dispatcher
) : BaseViewModel(dispatcher) {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    val currentColor: LiveResult<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(SuccessResult(it))
    }

    // --- пример результатов прослушивания через модельный слой

    init {
        colorsRepository.addListener(colorListener)
        load()
    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    // --- пример прослушивания результатов прямо с экрана

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = resources.getString(R.string.changed_color, result.name)
            toasts.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    /**
     * Пример использования плагинов побочных эффектов
     */
    fun requestPermission() = tasksFactory.async<Unit> {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val hasPermission = permissions.hasPermissions(permission)
        if (hasPermission) {
            dialogs.show(createPermissionAlreadyGrantedDialog()).await()
        } else {
            when (permissions.requestPermission(permission).await()) {
                PermissionStatus.GRANTED -> {
                    toasts.toast(resources.getString(R.string.permissions_grated))
                }
                PermissionStatus.DENIED -> {
                    toasts.toast(resources.getString(R.string.permissions_denied))
                }
                PermissionStatus.DENIED_FOREVER -> {
                    if (dialogs.show(createAskForLaunchingAppSettingsDialog()).await()) {
                        intents.openAppSettings()
                    }
                }
            }
        }
    }.safeEnqueue()

    fun tryAgain() {
        load()
    }

    private fun load() {
        colorsRepository.getCurrentColor().into(_currentColor)
    }

    private fun createPermissionAlreadyGrantedDialog() = DialogConfig(
        title = resources.getString(R.string.dialog_permissions_title),
        message = resources.getString(R.string.permissions_already_granted),
        positiveButton = resources.getString(R.string.action_ok)
    )

    private fun createAskForLaunchingAppSettingsDialog() = DialogConfig(
        title = resources.getString(R.string.dialog_permissions_title),
        message = resources.getString(R.string.open_app_settings_message),
        positiveButton = resources.getString(R.string.action_open),
        negativeButton = resources.getString(R.string.action_cancel)
    )
}