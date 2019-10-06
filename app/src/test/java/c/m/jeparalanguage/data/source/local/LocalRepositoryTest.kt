package c.m.jeparalanguage.data.source.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.paging.LimitOffsetDataSource
import androidx.test.core.app.ApplicationProvider
import c.m.jeparalanguage.data.source.local.entity.ContentEntity
import c.m.jeparalanguage.data.source.local.room.ContentDao
import c.m.jeparalanguage.data.source.local.room.ContentDatabase
import org.junit.*

class LocalRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

//    private val contentDao by inject<ContentDao>()
//    private val contentDatabase by inject<ContentDatabase>()

    private lateinit var contentDao: ContentDao
    private lateinit var contentDatabase: ContentDatabase


    @Before
    fun setUp() {
        //MockitoAnnotations.initMocks(this)

//        startKoin {
//            androidContext(Application())
//            modules(databaseTestModule)
//        }

        val context = ApplicationProvider.getApplicationContext<Context>()

        contentDatabase = Room.inMemoryDatabaseBuilder(
            context,
            ContentDatabase::class.java
        ).build()
        contentDao = contentDatabase.contentDao()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        contentDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeContentAndReadInList() {
        val contentEntity: ArrayList<ContentEntity> = arrayListOf()

        for (i in 1..3) {
            contentEntity.add(
                ContentEntity(
                    definition = "definition test",
                    phonetic = "phonetic test",
                    image = "image test",
                    id = "1 test",
                    word = "word test"
                )
            )
        }

        contentDao.insertContent(contentEntity)

        val byWord = contentDao.searchContent("word test")
        val listResult = (byWord.create() as LimitOffsetDataSource).loadRange(0, 10)
        Assert.assertEquals(listResult.get(0), contentEntity)
    }
}