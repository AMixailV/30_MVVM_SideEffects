package ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.views.changecolor

import ru.mixail_akulov.a30_mvvm_sideeffects.simplemvvm.model.colors.NamedColor

/**
 * Представляет элемент списка для цвета; его можно выбрать или нет
 */
data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean
)