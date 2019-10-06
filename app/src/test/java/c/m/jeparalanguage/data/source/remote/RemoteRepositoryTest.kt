package c.m.jeparalanguage.data.source.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import c.m.jeparalanguage.di.repositoryModule
import c.m.jeparalanguage.di.utilModule
import c.m.jeparalanguage.util.LiveDataTestUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RemoteRepositoryTest : KoinTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private val remoteRepository by inject<RemoteRepository>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(mainThreadSurrogate)

        startKoin {
            modules(
                listOf(
                    utilModule,
                    repositoryModule
                )
            )
        }
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        Dispatchers.resetMain()
        stopKoin()
    }

    @Test
    @Throws(IOException::class)
    fun getContentTestNotNull() = runBlocking {
        val contentList = remoteRepository.getContentAsLiveData()
        Assert.assertNotNull(LiveDataTestUtil.getValue(contentList))
    }
}