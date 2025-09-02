package eu.peernetwork.app.module.core

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.interactor.RemoteInteractor
import eu.peernetwork.app.interactor.SessionInteractorDelegate
import eu.peernetwork.app.interactor.SettingsInteractor
import eu.peernetwork.app.service.ResourceInteractorDelegate
import eu.peernetwork.core.common.interactor.SessionInteractor
import eu.peernetwork.core.common.provider.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module(includes = [
    NetworkModule::class,
    PersistenceModule::class,
    UsecaseModule::class,
    ServiceModule::class
])
object CoreModule {
    @Provides
    fun provideGson(): Gson = Gson()

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    @Provides
    fun provideSettingsInteractor(delegate: ResourceInteractorDelegate): RemoteInteractor = delegate

    @Provides
    fun provideDispatcher(): Dispatcher {
        return object : Dispatcher {
            override val io: CoroutineDispatcher = Dispatchers.IO
            override val main: CoroutineDispatcher = Dispatchers.Main
            override val default: CoroutineDispatcher = Dispatchers.Default
        }
    }

    @Provides
    fun bindSessionInteractor(delegate: SessionInteractorDelegate): SessionInteractor = delegate

    @Provides
    fun bindSettingsInteractor(delegate: SessionInteractorDelegate): SettingsInteractor = delegate
}
