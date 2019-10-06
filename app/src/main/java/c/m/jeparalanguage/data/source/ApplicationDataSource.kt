package c.m.jeparalanguage.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import c.m.jeparalanguage.data.source.local.entity.ContentEntity
import c.m.jeparalanguage.vo.Resource

interface ApplicationDataSource {
    fun getContent(): LiveData<Resource<PagedList<ContentEntity>>>
    fun searchContent(searchKeyword: String): LiveData<Resource<PagedList<ContentEntity>>>
}