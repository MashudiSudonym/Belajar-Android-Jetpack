package c.m.jeparalanguage.data.source.local.room

import androidx.paging.DataSource
import androidx.room.*
import c.m.jeparalanguage.data.source.local.entity.ContentEntity

@Dao
interface ContentDao {
    // Insert data from remote repository
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContent(contentEntity: List<ContentEntity>)

    // Delete data from local repository for refresh data
    @Query("DELETE FROM word_table")
    fun deleteContent()

    // transaction refresh data for local repository
    @Transaction
    fun updateContent(contentEntity: List<ContentEntity>) {
        deleteContent()
        insertContent(contentEntity)
    }

    // Get data from local repository
    @Query("SELECT * FROM word_table")
    fun getContent(): DataSource.Factory<Int, ContentEntity>

    // Search data from local repository
    @Query("SELECT * FROM word_table WHERE word LIKE '%' || :searchKeyword || '%'")
    fun searchContent(searchKeyword: String): DataSource.Factory<Int, ContentEntity>
}