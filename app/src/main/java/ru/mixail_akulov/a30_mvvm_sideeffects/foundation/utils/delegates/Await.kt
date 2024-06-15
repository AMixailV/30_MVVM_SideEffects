package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.utils.delegates

import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty

/**
 * Делегат свойства, который приостанавливает поток, пытающийся получить значение из свойства,
 * управляемого этим делегатом, если свойство содержит NULL.
 * Также только первое ненулевое присваивание обновляет значение свойства, все последующие присваивания игнорируются.
 */
class Await<T> {

    private val countDownLatch = CountDownLatch(1)
    private var value = AtomicReference<T>(null)

    operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        countDownLatch.await()
        return value.get()
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (value == null) return
        if (this.value.compareAndSet(null, value)) {
            countDownLatch.countDown()
        }
    }

}