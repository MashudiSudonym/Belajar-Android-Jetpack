package c.m.jeparalanguage.data.source.remote.response
import com.google.gson.annotations.SerializedName


data class ContentResponse(
    @SerializedName("definition")
    val definition: String? = "",
    @SerializedName("fonetik")
    val phonetic: String? = "",
    @SerializedName("gambar")
    val image: String? = "",
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("word")
    val word: String? = ""
)