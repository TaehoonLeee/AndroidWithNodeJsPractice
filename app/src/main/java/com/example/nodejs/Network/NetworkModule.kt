package com.example.nodejs.Network

import com.example.nodejs.Util.SelfSigningHelper
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.socket.client.Socket
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggerInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideCallFactory(httpLoggingInterceptor: HttpLoggingInterceptor) : Call.Factory {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi() : Moshi {
        return Moshi.Builder().build()
    }

    @Singleton
    @Provides
    fun provideMoshiConverterFactory() : MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRxJava3CallAdapterFactory() : RxJava3CallAdapterFactory {
        return RxJava3CallAdapterFactory.create()
    }

    @Singleton
    @Provides
    fun provideBaseUrl() : String {
        return "https://10.0.2.2:3000/"
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        httpLoggingInterceptor: Call.Factory,
        moshiConverterFactory: MoshiConverterFactory,
        rxJava3CallAdapterFactory: RxJava3CallAdapterFactory,
        baseUrl : String
    ) : Retrofit {
        return Retrofit.Builder()
            .callFactory(httpLoggingInterceptor)
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(rxJava3CallAdapterFactory)
            .client(createOkHttpClient())
            .baseUrl(baseUrl)
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val helper = SelfSigningHelper.getInstance()
        val builder = OkHttpClient.Builder()
        helper.setSSLOKHttp(builder, "10.0.2.2")

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideNodeService(retrofit: Retrofit) : NodeService {
        return retrofit.create(NodeService::class.java)
    }

    @Singleton
    @Provides
    fun provideSocketService() : SocketService {
        return SocketService()
    }

    @Singleton
    @Provides
    fun provideFcmService(retrofit: Retrofit) : FcmService {
        return retrofit.create(FcmService::class.java)
    }
}