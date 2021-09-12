package com.mobline.domain.base

import com.mobline.domain.exceptions.ErrorConverter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

typealias CompletionBlock<T> = BaseUseCase.Request<T>.() -> Unit

abstract class BaseUseCase<P, R>(
    private val executionContext: CoroutineContext,
    private val errorConverter: ErrorConverter,
) {
    protected abstract suspend fun executeOnBackground(params: P): R

    suspend fun execute(params: P, block: CompletionBlock<R> = {}) {
        val request = Request<R>().apply(block).also { it.onStart?.invoke() }
        try {
            val result = withContext(executionContext) { executeOnBackground(params) }
            request.onSuccess(result)
        } catch (e: CancellationException) {
            request.onCancel?.invoke(e)
        } catch (e: Throwable) {
            request.onError?.invoke(errorConverter.convert(e))
        } finally {
            request.onTerminate?.invoke()
        }
    }

    class Request<T> {
        var onSuccess: suspend (T) -> Unit = {}
        var onStart: (suspend () -> Unit)? = null
        var onError: (suspend (Throwable) -> Unit)? = null
        var onCancel: (suspend (CancellationException) -> Unit)? = null
        var onTerminate: (suspend () -> Unit)? = null
    }
}