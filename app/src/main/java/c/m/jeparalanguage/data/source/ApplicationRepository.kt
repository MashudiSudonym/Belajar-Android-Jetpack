package c.m.jeparalanguage.data.source

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import c.m.jeparalanguage.data.source.local.LocalRepository
import c.m.jeparalanguage.data.source.local.entity.ContentEntity
import c.m.jeparalanguage.data.source.remote.ApiResponse
import c.m.jeparalanguage.data.source.remote.RemoteRepository
import c.m.jeparalanguage.data.source.remote.response.ContentResponse
import c.m.jeparalanguage.util.ContextProviders
import c.m.jeparalanguage.util.isNetworkStatusAvailable
import c.m.jeparalanguage.vo.Resource

class ApplicationRepository(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val contextProviders: ContextProviders,
    private val context: Context
) : ApplicationDataSource {

    private val isOnline = context.isNetworkStatusAvailable()

    override fun getContent(): LiveData<Resource<PagedList<ContentEntity>>> =
        object :
            NetworkBoundResource<PagedList<ContentEntity>, List<ContentResponse>>(contextProviders) {
            override fun onFetchFailed(message: String?) {
                Log.e(LOG_TAG, message.toString())
            }

            override suspend fun createCall(): LiveData<ApiResponse<List<ContentResponse>>>? =
                remoteRepository.getContentAsLiveData()

            override fun shouldFetch(data: PagedList<ContentEntity>?): Boolean =
                data == null || data.isEmpty() || isOnline

            override fun loadFromDb(): LiveData<PagedList<ContentEntity>> =
                localRepository.getContent().toLiveData(pageSize = 20)

            override fun saveCallResult(item: List<ContentResponse>) {
                val contentEntity: ArrayList<ContentEntity> = arrayListOf()

                for (resultResponse in item) {
                    contentEntity.add(
                        ContentEntity(
                            definition = resultResponse.definition.toString(),
                            phonetic = resultResponse.phonetic.toString(),
                            image = resultResponse.image.toString(),
                            id = resultResponse.id.toString(),
                            word = resultResponse.word.toString()
                        )
                    )
                }

                localRepository.updateContent(contentEntity)
            }
        }.asLiveData()

    override fun searchContent(searchKeyword: String): LiveData<Resource<PagedList<ContentEntity>>> =
        object :
            NetworkBoundResource<PagedList<ContentEntity>, List<ContentResponse>>(contextProviders) {
            override fun onFetchFailed(message: String?) {
                Log.e(LOG_TAG, message.toString())
            }

            override suspend fun createCall(): LiveData<ApiResponse<List<ContentResponse>>>? = null

            override fun shouldFetch(data: PagedList<ContentEntity>?): Boolean = false

            override fun loadFromDb(): LiveData<PagedList<ContentEntity>> =
                localRepository.searchContent(searchKeyword).toLiveData(pageSize = 5)

            override fun saveCallResult(item: List<ContentResponse>) {}
        }.asLiveData()

    companion object {
        const val LOG_TAG = "Error Get Data"
    }
}