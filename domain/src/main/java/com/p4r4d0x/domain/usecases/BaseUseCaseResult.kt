package com.p4r4d0x.domain.usecases

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.*

abstract class BaseUseCaseNoResult {
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract suspend fun run()

    fun invoke(
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        onResult: (() -> Unit)? = null
    ) {
        if (onResult != null) {
            val job = scope.async(dispatcher) { run() }
            scope.launch(Dispatchers.Main) {
                job.await()
                onResult()
            }
        } else {
            scope.launch(Dispatchers.Main) {
                run()
            }
        }
    }

}

abstract class BaseUseCaseResult<Result> {
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract suspend fun run(): Result

    fun invoke(
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        resultCallback: (Result) -> Unit = {}
    ) {
        val job = scope.async(dispatcher) { run() }
        scope.launch(Dispatchers.Main) { resultCallback(job.await()) }
    }

}

abstract class BaseUseCaseParamsResult<Params, Result> {
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract suspend fun run(params: Params): Result

    fun invoke(
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        params: Params,
        resultCallback: (Result) -> Unit = {}
    ) {
        val job = scope.async(dispatcher) { run(params) }
        scope.launch(Dispatchers.Main) { resultCallback(job.await()) }
    }
}


