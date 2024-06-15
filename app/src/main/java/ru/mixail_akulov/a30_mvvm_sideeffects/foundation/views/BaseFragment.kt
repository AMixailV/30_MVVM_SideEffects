package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.ErrorResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.PendingResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.SuccessResult
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.Result
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.activity.ActivityDelegateHolder

/**
 * Base class for all fragments
 */
abstract class BaseFragment : Fragment() {

    /**
     * View-модель, которая управляет этим фрагментом
     */
    abstract val viewModel: BaseViewModel

    /**
     * Вызовите этот метод, когда элементы управления действиями (например, панель инструментов) должны быть повторно отображены.
     */
    fun notifyScreenUpdates() {
        (requireActivity() as ActivityDelegateHolder).delegate.notifyScreenUpdates()
    }

    /**
     * Скройте все views в [root], а затем вызовите одну из предоставленных
     * лямбда-функций в зависимости от [result]:
     * - [onPending] is called when [result] is [PendingResult]
     * - [onSuccess] is called when [result] is [SuccessResult]
     * - [onError] is called when [result] is [ErrorResult]
     */
    fun <T> renderResult(root: ViewGroup, result: Result<T>,
                         onPending: () -> Unit,
                         onError: (Exception) -> Unit,
                         onSuccess: (T) -> Unit) {

        root.children.forEach { it.visibility = View.GONE }
        when (result) {
            is SuccessResult -> onSuccess(result.data)
            is ErrorResult -> onError(result.exception)
            is PendingResult -> onPending()
        }
    }
}