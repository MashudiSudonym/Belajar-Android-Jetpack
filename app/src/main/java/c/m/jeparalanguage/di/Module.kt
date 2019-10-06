package c.m.jeparalanguage.di

import androidx.room.Room
import c.m.jeparalanguage.data.source.ApplicationRepository
import c.m.jeparalanguage.data.source.local.LocalRepository
import c.m.jeparalanguage.data.source.local.room.ContentDatabase
import c.m.jeparalanguage.data.source.remote.RemoteRepository
import c.m.jeparalanguage.data.source.remote.webservice.ClientServices
import c.m.jeparalanguage.ui.main.MainViewModel
import c.m.jeparalanguage.util.ContextProviders
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val utilModule = module {
    single { ContextProviders() }
    single { ClientServices() }
}

val repositoryModule = module {
    single { RemoteRepository(get()) }
    single { LocalRepository(get()) }
    single { ApplicationRepository(get(), get(), get(), androidContext()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ContentDatabase::class.java,
            "Jepara Language Database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    single { get<ContentDatabase>().contentDao() }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}