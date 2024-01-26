package tv.nsing.mediareader.core.di

import android.content.Context
import androidx.work.WorkManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GsonModule {
    @Provides
    fun provideGson() = Gson()
}