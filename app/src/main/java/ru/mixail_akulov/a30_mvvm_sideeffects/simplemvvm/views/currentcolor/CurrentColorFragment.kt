package ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.views.currentcolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mixail_akulov.a30_mvvm_sideeffects.databinding.FragmentCurrentColorBinding
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.BaseFragment
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.BaseScreen
import ru.mixail_akulov.a30_mvvm_sideeffects.foundation.views.screenViewModel
import ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.views.onTryAgain
import ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.views.renderSimpleResult


class CurrentColorFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CurrentColorViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentCurrentColorBinding.inflate(inflater, container, false)
        viewModel.currentColor.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = {
                    binding.colorView.setBackgroundColor(it.value)
                }
            )
        }

        binding.changeColorButton.setOnClickListener {
            viewModel.changeColor()
        }

        binding.askPermissionsButton.setOnClickListener {
            viewModel.requestPermission()
        }

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }

        return binding.root
    }
}