package c.m.jeparalanguage.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "word_table")
data class ContentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val idContentTable: Long = 0L,
    @ColumnInfo(name = "definition")
    val definition: String? = "",
    @ColumnInfo(name = "phonetic")
    val phonetic: String? = "",
    @ColumnInfo(name = "image")
    val image: String? = "",
    @ColumnInfo(name = "id")
    val id: String? = "",
    @ColumnInfo(name = "word")
    val word: String? = ""
): Parcelable