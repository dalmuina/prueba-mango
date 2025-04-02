package com.dalmuina.pruebamango

import android.app.Application
import com.dalmuina.pruebamango.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PruebaMangoApp: Application() {

    override fun onCreate(){
        super.onCreate()
        startKoin {
            androidContext(this@PruebaMangoApp)
            androidLogger()

            modules(appModule)
        }
    }
}
