package ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.views.changecolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.GridLayoutManager
import ru.mixail_akulov.a30_mvvm_sideeffects.R
import ru.mixail_akulov.a30_mvvm_sideeffects.databinding.FragmentChangeColorBinding
import ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.views.changecolor.ChangeColorViewModel
import ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.views.changecolor.ColorsAdapter
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.BaseFragment
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.BaseScreen
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.HasScreenTitle
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.screenViewModel
import ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.views.onTryAgain
import ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.views.renderSimpleResult

/**
 * Экран для изменения цвета.
 * 1) Отображает список доступных цветов
 * 2) Позволяет выбрать нужный цвет
 * 3) Выбранный цвет сохраняется только после нажатия кнопки "Сохранить"
 * 4) Текущий выбор сохраняется через [SavedStateHandle] (see [ChangeColorViewModel])
 */
class ChangeColorFragment : BaseFragment(), HasScreenTitle {

    /**
     * Этот экран имеет 1 аргумент: идентификатор цвета, который будет отображаться как выбранный.
     */
    class Screen(
        val currentColorId: Long
    ) : BaseScreen

    override val viewModel by screenViewModel<ChangeColorViewModel>()

    /**
     * Пример динамического заголовка экрана
     */
    override fun getScreenTitle(): String? = viewModel.screenTitle.value

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentChangeColorBinding.inflate(inflater, container, false)

        val adapter = ColorsAdapter(viewModel)
        setupLayoutManager(binding, adapter)

        binding.saveButton.setOnClickListener { viewModel.onSavePressed() }
        binding.cancelButton.setOnClickListener { viewModel.onCancelPressed() }

        viewModel.viewState.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(binding.root, result) { viewState ->
                adapter.items = viewState.colorsList
                binding.saveButton.visibility = if (viewState.showSaveButton) View.VISIBLE else View.INVISIBLE
                binding.cancelButton.visibility = if (viewState.showCancelButton) View.VISIBLE else View.INVISIBLE
                binding.saveProgressBar.visibility = if (viewState.showSaveProgressBar) View.VISIBLE else View.GONE
            }
        }

        viewModel.screenTitle.observe(viewLifecycleOwner) {
            // если заголовок экрана изменен -> необходимо уведомлять об обновлениях
            notifyScreenUpdates()
        }

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }

        return binding.root
    }

    private fun setupLayoutManager(binding: FragmentChangeColorBinding, adapter: ColorsAdapter) {
        // ожидание ширины списка
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.root.width
                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
                val columns = width / itemWidth
                binding.colorsRecyclerView.adapter = adapter
                binding.colorsRecyclerView.layoutManager = GridLayoutManager(requireContext(), columns)
            }
        })
    }
}