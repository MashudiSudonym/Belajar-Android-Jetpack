package c.m.jeparalanguage

import android.app.Application
import c.m.jeparalanguage.di.databaseModule
import c.m.jeparalanguage.di.repositoryModule
import c.m.jeparalanguage.di.utilModule
import c.m.jeparalanguage.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class JeparaLanguageApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Koin
        startKoin {
            androidLogger()
            androidContext(this@JeparaLanguageApplication)
            modules(
                listOf(
                    viewModelModule,
                    databaseModule,
                    utilModule,
                    repositoryModule
                )
            )
        }
    }
}