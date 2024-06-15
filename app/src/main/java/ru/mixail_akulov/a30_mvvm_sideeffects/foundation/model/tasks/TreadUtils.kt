package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model.tasks

/**
 * Общие методы работы с потоками.
 */
interface ThreadUtils {

    /**
     * Приостановить текущий поток на указанное время.
     */
    fun sleep(millis: Long)

    class Default : ThreadUtils {
        override fun sleep(millis: Long) {
            Thread.sleep(millis)
        }
    }

}