package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.model

typealias Mapper<Input, Output> = (Input) -> Output

/**
 * Базовый класс, представляющий результат некоторой асинхронной операции.
 * Наследники sealed class известны уже на этапе компиляции, с ними удобнее работать
 */
sealed class Result<T> {

    /**
     * Преобразуйте этот результат типа T в другой результат типа R:
     * - результат ошибки типа T преобразуется в результат ошибки типа R с тем же исключением
     * - ожидающий результат типа T преобразуется в ожидающий результат типа R
     * - успешный результат типа T преобразуется в успешный результат типа R,
     * где преобразование ([SuccessResult.data] из T в R выполняется [mapper]
     */
    fun <R> map(mapper: Mapper<T, R>? = null): Result<R> = when(this) {
        is PendingResult -> PendingResult()
        is ErrorResult -> ErrorResult(this.exception)
        is SuccessResult -> {
            if (mapper == null) throw IllegalArgumentException("Mapper should not be NULL for success result")
            SuccessResult(mapper(this.data))
        }
    }
}

/**
 * Operation has been finished
 */
sealed class FinalResult<T> : Result<T>()

/**
 * Подклассы - возможные состояния результата
 * Operation is in progress
 */
class PendingResult<T> : Result<T>()

/**
 * Operation has finished successfully
 */
class SuccessResult<T>(
    val data: T
) : FinalResult<T>()

/**
 * Operation has finished with error
 */
class ErrorResult<T>(
    val exception: Exception
) : FinalResult<T>()

/**
 * Получить значение успеха [Result], если это возможно; в противном случае вернуть NULL.
 */
fun <T> Result<T>?.takeSuccess(): T? {
    return if (this is SuccessResult)
        this.data
    else
        null
}