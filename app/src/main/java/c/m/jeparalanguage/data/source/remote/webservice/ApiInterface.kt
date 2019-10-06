package c.m.jeparalanguage.data.source.remote.webservice

import c.m.jeparalanguage.data.source.remote.response.ContentResponse
import retrofit2.http.GET

interface ApiInterface {
    @GET("loadData.php")
    suspend fun getContent(): List<ContentResponse>
}