package c.m.jeparalanguage.ui.main

import androidx.lifecycle.*
import androidx.paging.PagedList
import c.m.jeparalanguage.data.source.ApplicationRepository
import c.m.jeparalanguage.data.source.local.entity.ContentEntity
import c.m.jeparalanguage.vo.Resource
import kotlinx.coroutines.cancel

class MainViewModel(private val applicationRepository: ApplicationRepository) : ViewModel() {
    fun getContent(): LiveData<Resource<PagedList<ContentEntity>>> =
        applicationRepository.getContent()

    private val _searchKeyword: MutableLiveData<String> = MutableLiveData()
    val searchContent: LiveData<Resource<PagedList<ContentEntity>>> =
        Transformations.switchMap(_searchKeyword) {
            applicationRepository.searchContent(it)
        }

    fun getSearchKeyword(searchKeyword: String) {
        _searchKeyword.value = searchKeyword
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
