package com.p4r4d0x.skintker

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        try {
            val user = FirebaseAuth.getInstance().currentUser ?: return chain.proceed(request)
            val task: Task<GetTokenResult> = user.getIdToken(false)
            val tokenResult = Tasks.await(task, 20, TimeUnit.SECONDS)
            val token = tokenResult.token

            request = request.newBuilder().addHeader("Authorization", "Bearer $token").build()

            return chain.proceed(request)
        } catch (e: Exception) {
            return chain.proceed(request)
        }
    }
}