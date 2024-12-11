package com.example.automacorp.service

import com.automacorp.service.RoomsApiService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiServices {

    const val API_USERNAME = "user"
    const val API_PASSWORD = "password"

    // Interceptor for basic authentication
    class BasicAuthInterceptor(val username: String, val password: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain
                .request()
                .newBuilder()
                .header("Authorization", Credentials.basic(username, password))
                .build()
            return chain.proceed(request)
        }
    }


    // Retrofit instance with basic authentication
    val roomsApiService: RoomsApiService by lazy {

        val client = getUnsafeOkHttpClient()
            .addInterceptor(BasicAuthInterceptor(API_USERNAME, API_PASSWORD))
            .build()

        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .baseUrl("https://automacorp.devmind.cleverapps.io/api/") // Base URL for your API
            .build()
            .create(RoomsApiService::class.java)
    }

    // SSL settings for the OkHttpClient
    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        val trustManager = object : javax.net.ssl.X509TrustManager {
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
        }

        val sslContext = javax.net.ssl.SSLContext.getInstance("SSL").apply {
            init(null, arrayOf(trustManager), java.security.SecureRandom())
        }

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { hostname, _ -> hostname.contains("cleverapps.io") }
    }
}

