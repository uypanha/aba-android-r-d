package io.panha.rd_app.core.network

import io.panha.rd_app.core.APIConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    fun getInstance(apiConfig: APIConfig, also: (Retrofit.Builder) -> Unit = { }): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
    }

    fun getClient(): OkHttpClient {

        val mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .build()
    }
}