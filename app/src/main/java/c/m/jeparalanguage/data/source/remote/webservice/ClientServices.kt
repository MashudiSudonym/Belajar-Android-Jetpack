package c.m.jeparalanguage.data.source.remote.webservice

import c.m.jeparalanguage.BuildConfig

class ClientServices {
    private val apiService = RetrofitService.getInstance(BASE_URL_API).create(ApiInterface::class.java)

    suspend fun getContent() = apiService.getContent()

    companion object {
        const val BASE_URL_API = BuildConfig.BASE_URL_API
        const val BASE_URL_IMAGE = BuildConfig.BASE_URL_IMAGE
    }
}