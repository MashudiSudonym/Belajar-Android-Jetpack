package c.m.jeparalanguage.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import c.m.jeparalanguage.data.source.remote.ApiResponse
import c.m.jeparalanguage.data.source.remote.ApiStatusResponse
import c.m.jeparalanguage.util.ContextProviders
import c.m.jeparalanguage.vo.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

abstract class NetworkBoundResource<ResultType, RequestType> constructor(private val contextProviders: ContextProviders) {
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)

        val dbSource = this.loadFromDb()

        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)

            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) result.value = newValue
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = runBlocking { createCall() }

        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }

        apiResponse?.let { data ->
            result.addSource(data) { response ->
                result.removeSource(apiResponse)
                result.removeSource(dbSource)

                when (response.apiStatus) {
                    ApiStatusResponse.SUCCESS -> {
                        GlobalScope.launch(contextProviders.io) {
                            processResponse(response)?.let { saveCallResult(it) }
                            GlobalScope.launch(contextProviders.main) {
                                result.addSource(loadFromDb()) { newData ->
                                    setValue(Resource.success(newData))
                                }
                            }
                        }
                    }
                    ApiStatusResponse.EMPTY -> {
                        GlobalScope.launch(contextProviders.main) {
                            GlobalScope.launch(contextProviders.io) {
                                processResponse(response)?.let { saveCallResult(it) }
                                GlobalScope.launch(contextProviders.main) {
                                    result.addSource(loadFromDb()) { newData ->
                                        setValue(Resource.success(newData))
                                    }
                                }
                            }
                        }
                    }
                    ApiStatusResponse.ERROR -> {
                        onFetchFailed(response.message)
                        result.addSource(dbSource) { newData ->
                            setValue(Resource.error(response.message.toString(), newData))
                        }
                    }
                }
            }
        }
    }

    protected abstract fun onFetchFailed(message: String?)

    protected open fun processResponse(response: ApiResponse<RequestType>) = response.body

    protected abstract suspend fun createCall(): LiveData<ApiResponse<RequestType>>?

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun loadFromDb(): LiveData<ResultType>

    protected abstract suspend fun saveCallResult(item: RequestType)

    fun asLiveData() = result as LiveData<Resource<ResultType>>

}