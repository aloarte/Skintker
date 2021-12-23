package com.p4r4d0x.skintker.domain

import kotlinx.coroutines.*

abstract class BaseUseCase<Result> {

    protected abstract suspend fun run(): Result

    fun invoke(
        scope: CoroutineScope = GlobalScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        resultCallback: (Result) -> Unit = {}
    ) {
        val job = scope.async(dispatcher) { run() }
        scope.launch(Dispatchers.Main) { resultCallback(job.await()) }
    }
}

