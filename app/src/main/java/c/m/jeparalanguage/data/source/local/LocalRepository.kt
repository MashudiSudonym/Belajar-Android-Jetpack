package c.m.jeparalanguage.data.source.local

import androidx.paging.DataSource
import c.m.jeparalanguage.data.source.local.entity.ContentEntity
import c.m.jeparalanguage.data.source.local.room.ContentDao

class LocalRepository(private val contentDao: ContentDao) {
    // refresh local data
    suspend fun updateContent(contentEntity: List<ContentEntity>) =
        contentDao.updateContent(contentEntity)

    // read local data
    fun getContent(): DataSource.Factory<Int, ContentEntity> = contentDao.getContent()

    // search local data
    fun searchContent(searchKeyword: String): DataSource.Factory<Int, ContentEntity> =
        contentDao.searchContent(searchKeyword)
}