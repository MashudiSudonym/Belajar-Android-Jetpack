package c.m.jeparalanguage.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import c.m.jeparalanguage.data.source.remote.response.ContentResponse
import c.m.jeparalanguage.data.source.remote.webservice.ClientServices

class RemoteRepository(private val clientServices: ClientServices) {
    suspend fun getContentAsLiveData(): LiveData<ApiResponse<List<ContentResponse>>> {
        val contentResult: MutableLiveData<ApiResponse<List<ContentResponse>>> = MutableLiveData()

        try {
            val clientServiceGetContent = clientServices.getContent()

            if (clientServiceGetContent.isNullOrEmpty()) {
                contentResult.value = ApiResponse.empty("empty data", clientServiceGetContent)
            } else {
                contentResult.value = ApiResponse.success(clientServiceGetContent)
            }

        } catch (e: Exception) {
            Log.e("Error Remote Repository", e.localizedMessage as String)

            contentResult.value = ApiResponse.error("error : ${e.localizedMessage}", null)
        }

        return contentResult
    }
}